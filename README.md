# LG Connector
Connector for LG devices with [SmartThings](https://www.smartthings.com/getting-started)

Simplifies the setup of Xiaomi devices with SmartThings.<br/>
If LG Connector is installed, virtual devices are registered automatically by the LG Connector SmartApp.<br/>
You don't have to do anything to add LG devices in SmartThings IDE.

Please see the [prerequisites](#prerequisites) needed for this connector to work properly.

## Donation
If this project helps you, you can give me a cup of coffee<br/>
[![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://paypal.me/fison67)
<br/><br/>

# Table of contents

<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:0 orderedList:0 -->

- [Release Notes](#release-notes)
- [Documentation](#documentation)
	- [Management Web Desktop Version](#management-web-desktop-version)
	- [Management Web Mobile Version](#management-web-mobile-version)
	- [DTH Example](#dth-example)
	- [Supported devices](#supported-devices)
- [Installation](#installation)
	- [Prerequisites](#prerequisites)
	- [Docker Versions](#docker-versions)
		- [Stable Versions](#stable-versions)
	- [Install LG Connector API Server](#install-lg-connector-api-server)
		- [Raspberry Pi](#raspberry-pi)
		- [Synology NAS](#synology-nas)
		- [Linux x86 x64](#linux-x86-x64)
	- [LG Connector configuration](#lg-connector-configuration)
	- [Install Device Type Handler (DTH)](#install-device-type-handler-dth)
		- [Install DTH with GitHub Repo integration](#install-dth-with-github-repo-integration)
	- [Install SmartApp](#install-smartapp)
		- [Install SmartApp with GitHub Repo integration](#install-smartapp-with-github-repo-integration)
- [Library](#library)
- [License](#license)

<!-- /TOC -->



# Release Notes

### Version: 0.0.2
```
Added support devices.
```


# Documentation
<br/><br/>

## Management Web Desktop Version
<br/><br/>

## Management Web Mobile Version
<br/><br/>

## DTH Example
<br/><br/>

## Supported Devices
<br/><br/>



# Installation

## Prerequisites
* SmartThings account
* Local server (Synology NAS, Raspberry Pi, Linux Server) with Docker installed


## Docker Versions

### Stable Versions
| Docker tag |
| ------------- |
|fison67/lg-connector:0.0.2|
|fison67/lg-connector-rasp:0.0.2|


## Install LG Connector API Server
### Raspberry Pi
> Docker must be installed and running before continuing the installation.

```
sudo mkdir /docker
sudo mkdir /docker/lg-connector
sudo chown -R pi:pi /docker
docker pull fison67/lg-connector-rasp:0.0.2
docker run -d --restart=always -v /docker/lg-connector:/config --name=lg-connector-rasp --net=host fison67/lg-connector-rasp:0.0.2
```

### Synology NAS
> Docker must be installed and running before continuing the installation. <br/>

See the [manual](doc/install/synology/README.md) for details

```
1. Open Docker app in Synology Web GUI
2. Select the Registry tab in the left menu
3. Search for "fison67"
4. Select and download the "fison67/lg-connector" image (choose the "latest" tag for the stable version or see the Docker Versions section above for other versions/tags)
5. Select the Image tab in the left menu and wait for the image to fully download
6. Select the downloaded image and click on the Launch button
7. Give the Container a sensible name (e.g. "lg-connector")
8. Click on Advanced Settings
9. Check the "auto-restart" checkbox in the Advanced Settings tab
10. Click on Add Folder in the Volume tab and create a new folder (e.g. /docker/lg-connector) for the configuration files. Fill in "/config" in the Mount path field.
11.  Check the "Use the same network as Docker Host" checkbox in the Network tab
12. Click on Apply => Next => Apply
```

### Linux x86 x64
> Docker must be installed and running before continuing the installation.

```
sudo mkdir /docker
sudo mkdir /docker/lg-connector
docker pull fison67/lg-connector:0.0.2
docker run -d --restart=always -v /docker/lg-connector:/config --name=lg-connector --net=host fison67/lg-connector:0.0.2
```


## LG Connector configuration

```
1. Open LG Connector web settings page (http://X.X.X.X:30020/settings)
2. Default Login ID & Password is [ admin / 12345 ]
3. Select a System IP Address. & Press a Save button.
4. Open a LG Connector of Smartapp. Fill in the blanks. Press Save button.
5. Go to the Smartthinq tab of web setting page & Press a Get Token button. 
   You can see a new window.
   Login by your lg account. When you success a login, Copy an url of brower and paste it to box of Refresh Token.
5. Restart LG Connector Docker container
```

