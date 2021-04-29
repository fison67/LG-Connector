/**
 *  LG Connector (v.0.0.15)
 *
 * MIT License
 *
 * Copyright (c) 2018 fison67@nate.com
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
 
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

definition(
    name: "LG Connector",
    namespace: "fison67",
    author: "fison67",
    description: "A Connector between LG and ST",
    category: "My Apps",
    iconUrl: "https://images.techhive.com/images/article/2015/11/lg-logo-100629042-large.jpg",
    iconX2Url: "https://images.techhive.com/images/article/2015/11/lg-logo-100629042-large.jpg",
    iconX3Url: "https://images.techhive.com/images/article/2015/11/lg-logo-100629042-large.jpg",
    oauth: true
)

preferences {
   page(name: "mainPage")
   page(name: "monitorPage")
   page(name: "langPage")
}


def mainPage() {
	def languageList = ["English", "Korean"]
    dynamicPage(name: "mainPage", title: "LG Connector", nextPage: null, uninstall: true, install: true) {
    	
    	if(location.hubs.size() < 1) {
            section() {
                paragraph "[ERROR]\nSmartThings Hub not found.\nYou need a SmartThings Hub to use LG-Connector."
            }
            return
        }
        
   		section("Request New Devices"){
        	input "address", "text", title: "Server address", required: true
        	input "address2", "text", title: "Port forwarding Server address", required: false
            input(name: "selectedLang", title:"Select a language" , type: "enum", required: true, options: languageList, defaultValue: "English", description:"Language for DTH")
            input "devHub", "enum", title: "Hub", required: true, multiple: false, options: getHubs()
        	href url:"http://${settings.address}", style:"embedded", required:false, title:"Local Management", description:"This makes you easy to setup"
        	href url:"http://${settings.address2}", style:"embedded", required:false, title:"External Management", description:"This makes you easy to setup"
        }
        
       	section() {
            paragraph "View this SmartApp's configuration to use it in other places."
            href url:"${apiServerUrl("/api/smartapps/installations/${app.id}/config?access_token=${state.accessToken}")}", style:"embedded", required:false, title:"Config", description:"Tap, select, copy, then click \"Done\""
       	}
    }
}

def langPage(){
	dynamicPage(name: "langPage", title:"Select a Language") {
    	section ("Select") {
        	input "Korean",  title: "Korean", multiple: false, required: false
        }
    }
}

def installed() {
    log.debug "Installed with settings: ${settings}"
    
    if (!state.accessToken) {
        createAccessToken()
    }
    
    initialize()
}

def updated() {
    log.debug "Updated with settings: ${settings}"

    initialize()
}

def getServerAddress(){
     return settings.address
}

/**
* deviceNetworkID : Reference Device. Not Remote Device
*/
def getDeviceToNotifyList(deviceNetworkID){
	def list = []
	state.monitorMap.each{ targetNetworkID, _data -> 
        if(deviceNetworkID == _data.id){
        	def item = [:]
            item['id'] = 'lg-connector-' + targetNetworkID
            item['data'] = _data.data
            list.push(item)
        }
    }
    return list
}

def updateLanguage(){
    log.debug "Languge >> ${settings.selectedLang}"
    def list = getChildDevices()
    list.each { child ->
        try{
        	child.setLanguage(settings.selectedLang)
        }catch(e){
        	log.error "DTH is not supported to select language"
        }
    }
}

def updateExternalNetwork(){
	log.debug "External Network >> ${settings.externalAddress}"
    def list = getChildDevices()
    list.each { child ->
        try{
        	child.setExternalAddress(settings.externalAddress)
        }catch(e){
        	log.error "DTH is not supported to select external address"
        }
    }
}

def initialize() {
	log.debug "initialize"
    
    def options = [
     	"method": "POST",
        "path": "/settings/smartthings",
        "headers": [
        	"HOST": settings.address,
            "Content-Type": "application/json"
        ],
        "body":[
            "app_url":"${apiServerUrl}/api/smartapps/installations/",
            "app_id":app.id,
            "access_token":state.accessToken
        ]
    ]
    
    def myhubAction = new physicalgraph.device.HubAction(options, null, [callback: null])
    sendHubCommand(myhubAction)
    log.debug "server..."
    
    updateLanguage()
    updateExternalNetwork()
}

def dataCallback(physicalgraph.device.HubResponse hubResponse) {
    def msg, json, status
    try {
        msg = parseLanMessage(hubResponse.description)
        status = msg.status
        json = msg.json
        log.debug "${json}"
    } catch (e) {
        logger('warn', "Exception caught while parsing data: "+e);
    }
}

def getDataList(){
    def options = [
     	"method": "GET",
        "path": "/requestDevice",
        "headers": [
        	"HOST": settings.address,
            "Content-Type": "application/json"
        ]
    ]
    def myhubAction = new physicalgraph.device.HubAction(options, null, [callback: dataCallback])
    sendHubCommand(myhubAction)
}

def addDevice(){
    def address = params.address
    def type = params.type
    def version = params.version
    def name = params.name
    
    log.info("Try >> ADD LG Device ip=${address} type=${type} version=${version} name=${name}")
	
    def dni = "lg-connector-" + address
    log.debug("DNI >> " + dni)
    def chlid = getChildDevice(dni)
    if(!child){
    	def namespace = "fison67"
        def dth = ""
        if(type == "tv"){
        	dth = "LG TV"
        }else if(type == "washer" || type == "wash_tower_washer"){
        	dth = "LG Washer"
        }else if(type == "refrigerator"){
        	dth = "LG Refrigerator"
        }else if(type == "ac"){
        	dth = "LG Air Conditioner"
        }else if(type == "dryer" || type == "wash_tower_dryer"){
        	dth = "LG Dryer"
        }else if(type == "air_purifier"){
        	dth = "LG Air PuriFier"
        }else if(type == "dehumidifier"){
        	dth = "LG Dehumidifier"
        }else if(type == "kimchi_refrigerator"){
        	dth = "LG Kimchi Refrigerator"
        }else if(type == "robot_cleaner"){
        	dth = "LG Robot Cleaner"
        }else if(type == "code_zero"){
        	dth = "LG Code Zero"
        }else if(type == "styler"){
        	dth = "LG Styler"
        }else if(type == "water_purifier"){
        	dth = "LG Water Purifier"
        }else if(type == "dishwasher"){
        	namespace = "streamorange58819"
        	dth = "LG DishWasher"
        }else if(type == "cooktop"){
        	dth = "LG Cooktop"
        }else if(type == "ceiling_fan"){
        	dth = "LG Ceiling Fan"
        }else if(type == "oven"){
        	dth = "LG Oven"
	}
        
        def label = dth
        if(name != null){
        	label = name
        }
        
        if(version == "2"){
        	dth = dth + " v2"
        }
        log.debug "DTH : " + dth
        
        if(dth != ""){
        	def childDevice = addChildDevice(namespace, dth, dni, getLocationID(), [
                "label": label
            ])    
            childDevice.setInfo(settings.address, address)
            log.debug "Success >> ADD Device DNI=${dni} ${name}"

            try{ childDevice.setLanguage(settings.selectedLang) }catch(e){}
            render contentType: "application/javascript", data: new groovy.json.JsonOutput().toJson("result":"ok")
        }else{
            render contentType: "application/javascript", data: new groovy.json.JsonOutput().toJson("result":"nonExist")
        }
        
        
    }
}

def updateDevice(){
    def data = request.JSON
    log.debug data
    def address = data.id
    def dni = "lg-connector-" + address
    def chlid = getChildDevice(dni)
    if(chlid){
		chlid.setStatus(data)
    }else{
    	log.debug "No DTH..... ${data}"
    }
    def resultString = new groovy.json.JsonOutput().toJson("result":true)
    render contentType: "application/javascript", data: resultString
}

def getDeviceList(){
	def list = getChildDevices();
    def resultList = [];
    list.each { child ->
        def dni = child.deviceNetworkId
        log.debug dni
        resultList.push( dni.substring(13, dni.length()) );
    }
    
    def configString = new groovy.json.JsonOutput().toJson("list":resultList)
    render contentType: "application/javascript", data: configString
}

def authError() {
    [error: "Permission denied"]
}

def renderConfig() {
    def configJson = new groovy.json.JsonOutput().toJson([
        description: "LG Connector API",
        platforms: [
            [
                platform: "SmartThings LG Connector",
                name: "LG Connector",
                app_url: apiServerUrl("/api/smartapps/installations/"),
                app_id: app.id,
                access_token:  state.accessToken
            ]
        ],
    ])

    def configString = new groovy.json.JsonOutput().prettyPrint(configJson)
    render contentType: "text/plain", data: configString
}

def getLocationID(){
	def locationID = null
    try{ locationID = getHubID(devHub) }catch(err){}
    return locationID
}

def getHubs(){
	def list = []
    location.getHubs().each { hub ->
    	list.push(hub.name)
    }
    return list
}

def getHubID(name){
	def id = null
    location.getHubs().each { hub ->
    	if(hub.name == name){
        	id = hub.id
        }
    }
    return id
}

mappings {
    if (!params.access_token || (params.access_token && params.access_token != state.accessToken)) {
        path("/config")                         { action: [GET: "authError"] }
        path("/list")                         	{ action: [GET: "authError"]  }
        path("/update")                         { action: [POST: "authError"]  }
        path("/add")                         	{ action: [POST: "authError"]  }
    } else {
        path("/config")                         { action: [GET: "renderConfig"]  }
        path("/list")                         	{ action: [GET: "getDeviceList"]  }
        path("/update")                         { action: [POST: "updateDevice"]  }
        path("/add")                         	{ action: [POST: "addDevice"]  }
    }
}
