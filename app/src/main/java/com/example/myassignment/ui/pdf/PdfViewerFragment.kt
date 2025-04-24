package com.example.myassignment.ui.pdf

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myassignment.R
import com.example.myassignment.databinding.FragmentPdfViewerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL


class PdfViewerFragment : Fragment(R.layout.fragment_pdf_viewer) {
    private var _binding:FragmentPdfViewerBinding? = null
    private val binding get() = _binding!!

    private val remotePdfUrl = "https://fssservices.bookxpert.co/GeneratedPDF/Companies/nadc/2024-2025/BalanceSheet.pdf"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPdfViewerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        downloadAndShowPdf(remotePdfUrl)
    }

    private fun downloadAndShowPdf(url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.connect()

                val inputStream = connection.inputStream
                val file = File.createTempFile("temp", ".pdf")

                file.outputStream().use { fileOut ->
                    inputStream.copyTo(fileOut)
                }

                withContext(Dispatchers.Main) {
                    binding.pdfView.fromFile(file)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .load()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Failed to load PDF", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}