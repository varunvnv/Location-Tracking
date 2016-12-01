LOCATION TRACKING

**************************************************************************************************
Note: As soon as the main activity is created allow the following command to make user to allow access to use GPS of the mobile device:

ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

Once the user accepts, comment the above line to continue with using the app.
**************************************************************************************************

This app automatically stores the user's location. 
This app has three activities:
i) Local Database
ii) Server Database
iii) Query

These activities can be switched by the menu in the action bar on the top.

This app uses location API to collect the user's GPS information. The GPS information is collected in a background process using a Service class.
The service is started as soon as the application is started. A UI thread is used to avoid the blocking of the app as the service starts.
The GPS information is displayed in the local database side.

All the GPS information recorded is moved to a remote database on Firebase. The server database side downloads the full contents of the database and displays all the GPS entries.

A broadcast receiver is used to monitor the network connection type and shows the result in "Network Type".

When the phone is connected to Wi-Fi, the GPS information is stored in the database automatically. If the device is connected to mobile network, the information is stored in a local database.
On clicking the "SYNC" button, the device stores the contents of the local databse to remote server whether it is conected to Wi-Fi or mobile network.

When the device is uploading the contents to remote database, the "SERVER STATUS" is updated as "CONNECTED".

The "QUERY" in the menu allows the user to retrieve the information according to "netid" either in ascending or descending order. By default, all the location entries are stored with netid "nvv6".
