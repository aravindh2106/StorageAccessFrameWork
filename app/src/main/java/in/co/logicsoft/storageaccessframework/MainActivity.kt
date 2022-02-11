package `in`.co.logicsoft.storageaccessframework

import `in`.co.logicsoft.storageaccessframework.databinding.ActivityMainBinding
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.lang.StringBuilder
import kotlin.jvm.Throws

const val CREATE_DOCUMENT = 100
const val WRITE_DOCUMENT = 101
const val READ_DOCUMENT = 102

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.createBtn.setOnClickListener {
            val fileName = binding.fileName.text.toString()
            createFile(fileName)
        }
        binding.writeBtn.setOnClickListener {
            writeFile()
        }
        binding.readBtn.setOnClickListener {
            readFile()
        }
    }

    private fun readFile() {
        val readIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
        }
        startActivityForResult(readIntent, READ_DOCUMENT)
    }

    private fun writeFile() {
        val writeIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
        }
        startActivityForResult(writeIntent, WRITE_DOCUMENT)
    }

    private fun createFile(fileName: String) {
        val createIntent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
            putExtra(Intent.EXTRA_TITLE, fileName)
        }
        startActivityForResult(createIntent, CREATE_DOCUMENT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var fileUri: Uri? = null
        if (requestCode == CREATE_DOCUMENT && resultCode == RESULT_OK) {
            Log.i("STORAGE TAG", "file successfully created")
        } else if (requestCode == WRITE_DOCUMENT && resultCode == RESULT_OK) {
            if (data != null) {
                fileUri = data.data
            }
            writeExternalFile(fileUri!!)
        } else if (requestCode == READ_DOCUMENT && resultCode == RESULT_OK) {
            if (data != null) {
                fileUri = data.data
            }
            try {
                val readData = readExternalFile(fileUri!!)
                Log.i("STORAGE TAG", "File data::$readData")
                Toast.makeText(this,"$fileUri",Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun writeExternalFile(uri: Uri) {
        try {
            val contentResolver = applicationContext.contentResolver
            val parcelFileDescription = contentResolver.openFileDescriptor(uri, "w")
            val fileOutPutStream = FileOutputStream(parcelFileDescription!!.fileDescriptor)
            val dataToWrite: String = binding.contentEt.text.toString()
            fileOutPutStream.write(dataToWrite.toByteArray())
            fileOutPutStream.close()
            parcelFileDescription.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun readExternalFile(uri: Uri) {
        val contentResolver = applicationContext.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val bufferReader = inputStream!!.bufferedReader()
        val stringBuilder = StringBuilder()
        val text: String = bufferReader.readLine()
        stringBuilder.append(text)
        inputStream.close()
    }
}