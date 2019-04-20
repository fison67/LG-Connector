/**
 *  LG TV (v.0.0.2)
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

metadata {
	definition (name: "LG TV", namespace: "fison67", author: "fison67") {
        capability "Switch"
        capability "Switch Level"
        capability "Audio Volume"
        capability "Speech Synthesis"
        
        command "playText", ["string"]
        
        command "setTimeRemaining"
        command "stop"
        
        command "setStatus"
        command "mute"
        command "unmute"
        command "goTV"
        command "goNetflix"
        command "goHdmi1"
        command "goHdmi2"
        command "goHdmi3"
        command "goPooq"
        command "goTving"
        command "goWatcha"
        command "goYoutube"
        command "goMusic"
        command "goPhoto"
        command "goWeb", ["string"]
        
        command "mediaPlay"
        command "mediaPause"
        command "mediaStop"
	}

	simulator {
	}
    
	preferences {
        input name: "webURL", title:"Type a web url" , type: "string", required: false, defaultValue: "https://www.naver.com", description:"URL"
	}

}

def msToTime(duration) {
    def seconds = (duration%60).intValue()
    def minutes = ((duration/60).intValue() % 60).intValue()
    def hours = ( (duration/(60*60)).intValue() %24).intValue()

    hours = (hours < 10) ? "0" + hours : hours
    minutes = (minutes < 10) ? "0" + minutes : minutes
    seconds = (seconds < 10) ? "0" + seconds : seconds

    return hours + ":" + minutes + ":" + seconds
}

def stop() { 
	unschedule()
	state.timerCount = 0
	updateTimer()
}

def timer(){
	if(state.timerCount > 0){
    	state.timerCount = state.timerCount - 30;
        if(state.timerCount <= 0){
        	if(device.currentValue("switch") == "on"){
        		off()
            }
        }else{
        	runIn(30, timer)
        }
        updateTimer()
    }
}

def updateTimer(){
    def timeStr = msToTime(state.timerCount)
    sendEvent(name:"leftTime", value: "${timeStr}", displayed:false)
    sendEvent(name:"timeRemaining", value: Math.round(state.timerCount/60), displayed:false)
}

def processTimer(second){
	if(state.timerCount == null){
    	state.timerCount = second;
    	runIn(30, timer)
    }else if(state.timerCount == 0){
		state.timerCount = second;
    	runIn(30, timer)
    }else{
    	state.timerCount = second
    }
    updateTimer()
}

def setTimeRemaining(time) { 
	if(time > 0){
        log.debug "Set a Timer ${time}Mins"
        processTimer(time * 60)
        setPowerByStatus(true)
    }
}

def setPowerByStatus(turnOn){
	if(device.currentValue("switch") == (turnOn ? "off" : "on")){
        if(turnOn){
        	on()
        }else{
        	off()
        }
    }
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

def updated() {
}

def setInfo(String app_url, String address) {
	log.debug "${app_url}, ${id}"
	state.app_url = app_url
    state.id = address
}

def setData(dataList){
	for(data in dataList){
        state[data.id] = data.code
    }
}

def setStatus(data){
	log.debug "${data}"
    def jsonObj = data
    
	if(jsonObj.cmd == "notify"){
        switch(jsonObj.key){
        case "power":
			sendEvent(name:"switch", value: jsonObj.data )
            break; 
        case "volume":
			sendEvent(name:"volume", value: jsonObj.data as int )
            break;
        case "mute":
			sendEvent(name:"mute", value: jsonObj.data ? "mute" : "unmuted" )
            break;
        case "input":
        	updateInput(jsonObj.data)
            break;
        case "channelNumber":
			sendEvent(name:"channelNumber", value: jsonObj.data )
            break;
        case "channelName":
			sendEvent(name:"channelName", value: jsonObj.data )
            break;
        }
        updateLastTime();
    }
}

def updateInput(name){
	def title = name;
    switch(name){
   	case "com.webos.app.livetv":
    	title = "LIVE TV"
        break;
   	case "netflix":
    	title = "Netflix"
        break;
   	case "pooq":
    	title = "POOQ"
        break;
   	case "cj.eandm":
    	title = "TVING"
        break;
   	case "com.frograms.watchaplay.webos":
    	title = "Watcha"
        break;
   	case "youtube.leanback.v4":
    	title = "Youtube"
        break;
   	case "com.webos.app.hdmi1":
    	title = "HDMI #1"
        break;
   	case "com.webos.app.hdmi2":
    	title = "HDMI #2"
        break;
   	case "com.webos.app.hdmi3":
    	title = "HDMI #3"
        break;
   	case "com.webos.app.browser":
    	title = "Internet"
        break;
   	case "com.webos.app.music":
    	title = "Music"
        break;
   	case "com.webos.app.photovideo":
    	title = "Photo & Video"
        break;
    }
	sendEvent(name:"inputSource", value: title)
    
    
}

def updateLastTime(){
	def now = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)
    sendEvent(name: "lastCheckin", value: now, displayed:false)
}

def on(){
	makeCommand("power", "on")
}

def off(){
	makeCommand("power", "off")
}

def channelUp(){
	makeCommand("channelUp", "")
}

def channelDown(){
	makeCommand("channelDown", "")
}

/**
* Not yet
*/
def setTvChannel(channel){
	makeCommand("channel", channel)
}

def mute(){
	makeCommand("mute", true)
}

def unmute(){
	makeCommand("mute", false)
}

def setVolume(volume){
    makeCommand("volume", volume as int)
}

def volumeUp(){
	makeCommand("volume", (device.currentValue("volume") as int) + 1)
}

def volumeDown(){
	makeCommand("volume", (device.currentValue("volume") as int) - 1)
}

def mediaPlay(){
	makeCommand("play", "")
}

def mediaPause(){
	makeCommand("pause", "")
}

def mediaStop(){
	makeCommand("stop", "")
}

def goNetflix(){
	makeCommand("input", "netflix")
}

def goTV(){
	makeCommand("input", "com.webos.app.livetv")
}

def goHdmi1(){
	makeCommand("input", "com.webos.app.hdmi1")
}

def goHdmi2(){
	makeCommand("input", "com.webos.app.hdmi2")
}

def goHdmi3(){
	makeCommand("input", "com.webos.app.hdmi3")
}

def goPooq(){
	makeCommand("input", "pooq")
}

def goTving(){
	makeCommand("input", "cj.eandm")
}

def goWatcha(){
	makeCommand("input", "com.frograms.watchaplay.webos")
}

def goYoutube(){
	makeCommand("input", "youtube.leanback.v4")
}

def goMusic(){
	makeCommand("input", "com.webos.app.music")
}

def goPhoto(){
	makeCommand("input", "com.webos.app.photovideo")
}

def goWeb(_url){
	def url = _url
	if(url == null){
    	url = settings.webURL
        if(url == null){
        	url = "http://www.naver.com"
        }
    }
	makeCommand("web", url)
}


def playText(text){
	makeCommand("message", text)
}

def makeCommand(type, value){
    def body = [
        "id": state.id,
        "cmd": type,
        "data": value
    ]
    def options = makeCommand(body)
    sendCommand(options, null)
}

def makeCommand(body){
	def options = [
     	"method": "POST",
        "path": "/tv/control",
        "headers": [
        	"HOST": state.app_url,
            "Content-Type": "application/json"
        ],
        "body":body
    ]
    return options
}

def sendCommand(options, _callback){
	def myhubAction = new hubitat.device.HubAction(options, null, [callback: _callback])
    sendHubCommand(myhubAction)
}

