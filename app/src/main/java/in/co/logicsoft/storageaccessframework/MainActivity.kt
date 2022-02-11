package `in`.co.logicsoft.storageaccessframework

import `in`.co.logicsoft.storageaccessframework.databinding.ActivityMainBinding
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import java.io.FileOutputStream
import java.io.IOException

const val CREATE_DOCUMENT = 100
const val WRITE_DOCUMENT = 101
const val READ_DOCUMENT = 102
const val OPEN_DOCUMENT_TREE = 103

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val viewModel: StorageViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@MainActivity
            viewmodel = viewModel
        }
        setContentView(binding.root)
        subscribeUi()
    }


    @RequiresApi(Build.VERSION_CODES.R)
    private fun subscribeUi() {
        viewModel.fileCreate.observe(this, Observer {
            val fileName = binding.fileName.text.toString()
            createFile(fileName)
        })
        viewModel.fileWrite.observe(this, Observer {
            writeFile()
        })
        viewModel.fileRead.observe(this, Observer {
            readFile()
        })
        viewModel.openDirectory.observe(this, Observer {
            openDirectory()
        })

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

    @RequiresApi(Build.VERSION_CODES.R)
    private fun openDirectory() {
        val createIntent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        }
        startActivityForResult(createIntent, OPEN_DOCUMENT_TREE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var fileUri: Uri? = null
        if (requestCode == CREATE_DOCUMENT && resultCode == RESULT_OK) {
            fileUri = data!!.data
            Log.i("STORAGE TAG", "file successfully created")
            Toast.makeText(this, "$fileUri", Toast.LENGTH_LONG).show()

        } else if (requestCode == WRITE_DOCUMENT && resultCode == RESULT_OK) {
            if (data != null) {
                fileUri = data.data
            }
            writeExternalFile(fileUri!!)
            Toast.makeText(this, "$fileUri", Toast.LENGTH_LONG).show()
        } else if (requestCode == READ_DOCUMENT && resultCode == RESULT_OK) {
            if (data != null) {
                fileUri = data.data
            }
            try {
                val readData = readExternalFile(fileUri!!)
                Log.i("STORAGE TAG", "File data::$readData")
                Toast.makeText(this, "$fileUri", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (requestCode == OPEN_DOCUMENT_TREE && resultCode == RESULT_OK) {
            if (data != null) {
                fileUri = data.data
            }

            contentResolver.takePersistableUriPermission(
                fileUri!!,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            Toast.makeText(this, "$fileUri", Toast.LENGTH_LONG).show()
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