package com.example.picsum.ui.image

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.picsum.R
import com.example.picsum.base.BaseFragment
import com.example.picsum.databinding.FragmentImageBinding
import com.example.picsum.ext.formatPhotoUrl
import com.example.picsum.ext.showToast
import dagger.hilt.android.AndroidEntryPoint
import com.example.picsum.ui.image.ImageViewModel.UIEvent
import com.google.android.material.snackbar.Snackbar

@AndroidEntryPoint
class ImageFragment : BaseFragment<FragmentImageBinding>(R.layout.fragment_image) {

    private val viewModel: ImageViewModel by viewModels()
    private val downloadManager: DownloadManager by lazy {
        requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerDownloadReceiver()

        binding.vm = viewModel

        viewModel.uiEvent.asLiveData().observe(viewLifecycleOwner, {
            handleUIEvent(it)
        })
    }

    override fun onDestroyView() {
        unRegisterDownloadReceiver()
        super.onDestroyView()
    }

    private fun handleUIEvent(uiEvent: UIEvent) {
        when (uiEvent) {
            is UIEvent.ImageError -> {
                requireContext().showToast(getString(R.string.exception_error))
                findNavController().popBackStack()
            }
            is UIEvent.Back -> findNavController().popBackStack()
            is UIEvent.NavImageMore -> findNavController().navigate(ImageFragmentDirections.actionImageFragmentToImageMoreBottomFragment())
            is UIEvent.NavImageEffect -> findNavController().navigate(
                ImageFragmentDirections.actionImageFragmentToImageEffectBottomFragment(
                    uiEvent.image
                )
            )
            is UIEvent.FileDownload -> processFileDownload()
            is UIEvent.OpenWebLink -> {
                openWebLink(uiEvent.url)
            }
            is UIEvent.Share -> {
                shareIntent(uiEvent.url)
            }
        }
    }

    private fun shareIntent(url: String) {
        startActivity(
            Intent.createChooser(
                Intent(Intent.ACTION_SEND).apply {
                    type = SHARE_TYPE
                    putExtra(Intent.EXTRA_TEXT, url)
                },
                getString(R.string.share)
            )
        )
    }

    private fun openWebLink(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }


    private fun processFileDownload() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionResult.launch(permission)
                return
            }
        }
        downloadFile()
    }

    // 다운로드 결과 result
    private val permissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                downloadFile()
            } else {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.file_download),
                    Snackbar.LENGTH_LONG
                ).setAction(getString(R.string.setting)) {
                    activity?.let {
                        val uri = Uri.fromParts(
                            "package",
                            it.packageName,
                            null
                        )
                        startActivity(
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = uri
                            }
                        )
                    }
                }.show()
            }
        }

    // File 다운로드
    private fun downloadFile() {
        viewModel.image.value?.let { photo ->
            val downloadUrl = photo.downloadUrl.formatPhotoUrl(photo.grayScale, photo.blur)
            val fileName = "${photo.id}_${System.currentTimeMillis()}.jpg"
            val request = DownloadManager.Request(Uri.parse(downloadUrl))
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                .setTitle(getString(R.string.app_name))
                .setDescription(fileName)
                .setMimeType(IMAGE_MIMETYPE)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DCIM, fileName)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            downloadManager.enqueue(request)
        }
    }

    // File 다운로드 완료 receiver
    private var onDownloadComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == intent.action) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                val query = DownloadManager.Query()
                query.setFilterById(id)
                val cursor: Cursor = downloadManager.query(query)
                if (!cursor.moveToFirst()) {
                    return
                }
                val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val status = cursor.getInt(columnIndex)
                Environment.getRootDirectory()
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    requireContext().showToast(getString(R.string.photo_download_complete))
                } else {
                    requireContext().showToast(getString(R.string.photo_download_failed))
                }
            }
        }
    }

    private fun registerDownloadReceiver() {
        val intentFilter = IntentFilter().apply {
            addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
        }
        requireActivity().registerReceiver(onDownloadComplete, intentFilter)
    }

    private fun unRegisterDownloadReceiver() {
        requireActivity().unregisterReceiver(onDownloadComplete)
    }

    companion object {
        const val SHARE_TYPE = "text/plain"
        const val IMAGE_MIMETYPE = "image/jpeg"
    }
}