/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lx.red

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.media.session.PlaybackState.STATE_NONE
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import com.lx.red.common.logger.Log
import kotlinx.android.synthetic.main.fragment_bluetooth_chat.*
import kotlinx.android.synthetic.main.fragment_bluetooth_chat.view.*


class BluetoothChatFragment : Fragment() {
    private val TAG = "BluetoothChatFragment"

    // Intent request codes
    private val REQUEST_CONNECT_DEVICE_SECURE = 1
    private val REQUEST_CONNECT_DEVICE_INSECURE = 2
    private val REQUEST_ENABLE_BT = 3

    // Layout Views
    private var mConversationView: ListView? = null
    private var mOutEditText: EditText? = null
    private var mSendButton: Button? = null
    private var mhelp: Button? = null
    private var mhelp2: Button? = null
    private var mhelp3: Button? = null
    private var mConnectedDeviceName: String? = null
    private var mConversationArrayAdapter: ArrayAdapter<String>? = null
    private var mOutStringBuffer: StringBuffer? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mChatService: BluetoothChatService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        // If the adapter is null, then Bluetooth is not supported
        val activity = getActivity()
        if (mBluetoothAdapter == null && activity != null) {
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show()
            activity.finish()
        }
    }

    override fun onStart() {
        super.onStart()
        if (mBluetoothAdapter == null) {
            return
        }
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter!!.isEnabled) {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
            setupChat()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mChatService != null) {
            mChatService!!.stop()
        }
    }

    override fun onResume() {
        super.onResume()

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService!!.mState == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService!!.start()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bluetooth_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mConversationView = view.findViewById(R.id.`in`)
        mOutEditText = view.findViewById(R.id.editTextOut)
        mSendButton = view.findViewById(R.id.buttonSend)
        mhelp = view.findViewById(R.id.help)
        mhelp2 = view.findViewById(R.id.help2)
        mhelp3 = view.findViewById(R.id.help3)
    }

    /**
     * Set up the UI and background operations for chat.
     */
    private fun setupChat() {
        Log.d(TAG, "setupChat()")

        val activity = getActivity() ?: return
        mConversationArrayAdapter = ArrayAdapter(activity, R.layout.message)
        mConversationView!!.adapter = mConversationArrayAdapter

        // Initialize the compose field with a listener for the return key
        mOutEditText!!.setOnEditorActionListener(mWriteListener)

        // Initialize the send button with a listener that for click events
        mSendButton!!.setOnClickListener { // Send a message using content of the edit text widget
            val view = view
            if (null != view) {
                val textView = view.findViewById<TextView>(R.id.editTextOut)
                val message = textView.text.toString()
                sendMessage(message)
            }
        }
        mhelp?.setOnClickListener { // Send a message using content of the edit text widget
            val view = view
            if (null != view) {
                val message = view.findViewById<Button>(R.id.help).text.toString()
                sendMessage(message)
            }
        }
        mhelp2?.setOnClickListener { // Send a message using content of the edit text widget
            val view = view
            if (null != view) {
                val message = view.findViewById<Button>(R.id.help2).text.toString()
                sendMessage(message)
            }
        }
        mhelp3?.setOnClickListener { // Send a message using content of the edit text widget
            val view = view
            if (null != view) {
                val message = view.findViewById<Button>(R.id.help3).text.toString()
                sendMessage(message)
            }
        }

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = BluetoothChatService(activity, mHandler)

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = StringBuffer()
    }

    /**
     * Makes this device discoverable for 300 seconds (5 minutes).
     */
    @SuppressLint("MissingPermission")
    private fun ensureDiscoverable() {
        if (mBluetoothAdapter!!.scanMode !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE
        ) {
            val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
            startActivity(discoverableIntent)
        }
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private fun sendMessage(message: String) {
        // Check that we're actually connected before trying anything
        if (mChatService!!.mState != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show()
            return
        }

        // Check that there's actually something to send
        if (message.length > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            val send = message.toByteArray()
            mChatService!!.write(send)

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer!!.setLength(0)
            mOutEditText!!.setText(mOutStringBuffer)
        }
    }

    /**
     * The action listener for the EditText widget, to listen for the return key
     */
    private val mWriteListener =
        OnEditorActionListener { view, actionId, event -> // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.action == KeyEvent.ACTION_UP) {
                val message = view.text.toString()
                sendMessage(message)
            }
            true
        }

    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
    private fun setStatus(resId: Int) {
        val activity = getActivity() ?: return
        val actionBar = activity.actionBar ?: return
        actionBar.setSubtitle(resId)
    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private fun setStatus(subTitle: CharSequence) {
        val activity = getActivity() ?: return
        val actionBar = activity.actionBar ?: return
        actionBar.subtitle = subTitle
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val activity = getActivity()
            when (msg.what) {
                Constants.MESSAGE_STATE_CHANGE -> when (msg.arg1) {
                    BluetoothChatService.STATE_CONNECTED -> {
                        setStatus(getString(R.string.title_connected_to, mConnectedDeviceName))
                        mConversationArrayAdapter!!.clear()
                    }
                    BluetoothChatService.STATE_CONNECTING -> setStatus(R.string.title_connecting)
                    BluetoothChatService.STATE_LISTEN, BluetoothChatService.STATE_NONE -> setStatus(
                        R.string.title_not_connected
                    )
                }
                Constants.MESSAGE_WRITE -> {
                    val writeBuf = msg.obj as ByteArray
                    // construct a string from the buffer
                    val writeMessage = String(writeBuf)
                    mConversationArrayAdapter!!.add("Me:  $writeMessage")
                }
                Constants.MESSAGE_READ -> {
                    val readBuf = msg.obj as ByteArray
                    // construct a string from the valid bytes in the buffer
                    val readMessage = String(readBuf, 0, msg.arg1)
                    mConversationArrayAdapter!!.add("$mConnectedDeviceName:  $readMessage")
                }
                Constants.MESSAGE_DEVICE_NAME -> {
                    // save the connected device's name
                    mConnectedDeviceName = msg.data.getString(Constants.DEVICE_NAME)
                    if (null != activity) {
                        Toast.makeText(
                            activity, "Connected to "
                                    + mConnectedDeviceName, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                Constants.MESSAGE_TOAST -> if (null != activity) {
                    Toast.makeText(
                        activity, msg.data.getString(Constants.TOAST),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CONNECT_DEVICE_SECURE ->                 // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true)
                }
            REQUEST_CONNECT_DEVICE_INSECURE ->                 // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false)
                }
            REQUEST_ENABLE_BT ->                 // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat()
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled")
                    val activity = getActivity()
                    if (activity != null) {
                        Toast.makeText(
                            activity, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT
                        ).show()
                        activity.finish()
                    }
                }
        }
    }

    /**
     * Establish connection with other device
     *
     * @param data   An [Intent] with [DeviceListActivity.EXTRA_DEVICE_ADDRESS] extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private fun connectDevice(data: Intent?, secure: Boolean) {
        // Get the device MAC address
        val extras = data!!.extras ?: return
        val address = extras.getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS)
        // Get the BluetoothDevice object
        val device = mBluetoothAdapter!!.getRemoteDevice(address)
        // Attempt to connect to the device
        mChatService!!.connect(device, secure)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.bluetooth_chat, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.secure_connect_scan -> {

                // Launch the DeviceListActivity to see devices and do scan
                val serverIntent = Intent(getActivity(), DeviceListActivity::class.java)
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE)
                return true
            }
            R.id.insecure_connect_scan -> {

                // Launch the DeviceListActivity to see devices and do scan
                val serverIntent = Intent(getActivity(), DeviceListActivity::class.java)
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE)
                return true
            }
            R.id.discoverable -> {

                // Ensure this device is discoverable by others
                ensureDiscoverable()
                return true
            }
        }
        return false
    }

    companion object {
        private const val TAG = "BluetoothChatFragment"

        // Intent request codes
        private const val REQUEST_CONNECT_DEVICE_SECURE = 1
        private const val REQUEST_CONNECT_DEVICE_INSECURE = 2
        private const val REQUEST_ENABLE_BT = 3
    }

}