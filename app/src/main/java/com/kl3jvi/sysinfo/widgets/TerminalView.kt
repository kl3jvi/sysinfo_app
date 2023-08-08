package com.kl3jvi.sysinfo.widgets

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View.OnKeyListener
import androidx.appcompat.widget.AppCompatEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter

class TerminalView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var reader: BufferedReader? = null
    private var writer: PrintWriter? = null

    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        isVerticalScrollBarEnabled = true
        setBackgroundColor(Color.BLACK) // Set the background to black
        setTextColor(Color.WHITE) // Set the text color to green
        gravity = 0x30
        imeOptions = 0x00000006
        typeface = Typeface.create("monospace", Typeface.NORMAL)
        setOnKeyListener(
            OnKeyListener { v, keyCode, event -> // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    // Perform action on key press
                    executeCommand(text.toString())
                    return@OnKeyListener true
                }
                false
            }
        )
        // Add a text watcher to ensure the "$: " is always present
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString().startsWith("$: ")) {
                    text?.insert(0, "$: ")
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Keep the cursor always at the end
                text?.length?.let { setSelection(it) }
            }
        })

        // Always show "$: " at the beginning
        text?.append("$: ")
    }

    private fun executeCommand(command: String) {
        scope.launch {
            try {
                // Runtime.exec() creates a native process to run the shell command
                val process = Runtime.getRuntime().exec(command)

                // BufferedReader reads the output of the command
                val reader = BufferedReader(InputStreamReader(process.inputStream))

                // Append each line of output to result
                val result = StringBuilder()
                var line: String? = reader.readLine()
                while (line != null) {
                    result.append(line + "\n")
                    line = reader.readLine()
                }

                // Wait for the command to finish
                process.waitFor()

                // Display the output in your terminal
                withContext(Dispatchers.Main) {
                    append("\n$result")
                }
            } catch (e: Exception) {
                // Handle the exception
                withContext(Dispatchers.Main) {
                    append("\nError executing command: $command\n${e.message}")
                }
            }
        }
    }

    @OptIn(InternalCoroutinesApi::class)
    fun start(command: String) {
        // Create the shell process
        val process = Runtime.getRuntime().exec("sh")
        reader = BufferedReader(InputStreamReader(process.inputStream))
        writer = PrintWriter(process.outputStream)

        // Start reading the output
        scope.launch(Dispatchers.IO) {
            try {
                // Pass command to shell
                writer?.println(command)
                writer?.flush()

                reader?.use {
                    var line: String?
                    while (reader?.readLine().also { line = it } != null) {
                        val safeLine = line
                        withContext(Dispatchers.Main) {
                            append("\n$safeLine")
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun sendCommand(command: String?) {
        writer?.println(command)
        writer?.flush()
    }
}
