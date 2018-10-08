/**
 *  LG TV (v.0.0.1)
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
        capability "Configuration"
        capability "Tv Channel"
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

	tiles(scale: 2) {
		
        multiAttributeTile(name:"switch", type: "generic", width: 6, height: 2){
			tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
             	attributeState "on", label:'${name}', action:"switch.off", icon:"https://github.com/fison67/LG-Connector/blob/master/icons/lgtv-on.png?raw=true", backgroundColor:"#00a0dc", nextState:"turningOff"
                attributeState "off", label:'${name}', action:"switch.on", icon:"https://github.com/fison67/LG-Connector/blob/master/icons/lgtv-off.png?raw=true", backgroundColor:"#ffffff", nextState:"turningOn"
                
                attributeState "turningOn", label:'${name}', action:"switch.off", icon:"https://github.com/fison67/LG-Connector/blob/master/icons/lgtv-on.png?raw=true", backgroundColor:"#00a0dc", nextState:"turningOff"
                attributeState "turningOff", label:'${name}', action:"switch.ofn", icon:"https://github.com/fison67/LG-Connector/blob/master/icons/lgtv-off.png?raw=true", backgroundColor:"#ffffff", nextState:"turningOn"
			}
            
			tileAttribute("device.lastCheckin", key: "SECONDARY_CONTROL") {
    			attributeState("default", label:'Updated: ${currentValue}')
            }
            tileAttribute ("device.level", key: "SLIDER_CONTROL") {
                attributeState "level", action:"setLevel"
            }     
		}
        
        standardTile("mute", "device.mute") {
			state "unmuted", label:'${name}', action: "mute", nextState:"mute", icon: "st.custom.sonos.unmuted", backgroundColor:"#ffffff"
			state "mute", label:'${name}', action: "unmute", nextState:"unmuted", icon: "st.custom.sonos.muted", backgroundColor:"#f91818"
		}
        
        valueTile("inputSource", "device.inputSource", decoration: "flat", width: 5, height: 1) {
            state "default", label:'${currentValue}'
        }
        
        standardTile("inputHdmi1", "device.inputHdmi1") {
			state "inputHdmi1", label:'HDMI#1', action: "goHdmi1", backgroundColor:"#ffffff"
		}
        standardTile("inputHdmi2", "device.inputHdmi2") {
			state "inputHdmi2", label:'HDMI#2', action: "goHdmi2", backgroundColor:"#ffffff"
		}
        standardTile("inputHdmi3", "device.inputHdmi3") {
			state "inputHdmi3", label:'HDMI#3', action: "goHdmi3", backgroundColor:"#ffffff"
		}
        standardTile("inputMusic", "device.inputMusic") {
			state "inputMusic", label:'Music', action: "goMusic", backgroundColor:"#ffffff"
		}
        standardTile("inputYoutube", "device.inputYoutube") {
			state "inputYoutube", label:'Youtube', action: "goYoutube", backgroundColor:"#ffffff"
		}
        standardTile("inputNetflix", "device.inputNetflix") {
			state "inputNetflix", label:'Netflix', action: "goNetflix", backgroundColor:"#ffffff"
		}
        
        
        standardTile("inputTV", "device.inputTV") {
			state "inputTV", label:'TV', action: "goTV", backgroundColor:"#ffffff"
		}
        valueTile("channelName", "device.channelName", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }
        valueTile("channelNumber", "device.channelNumber", decoration: "flat", width: 2, height: 1) {
            state "default", label:'${currentValue}'
        }
        standardTile("channelUp", "device.channelUp") {
			state "channelUp", label:'CH UP', action: "channelUp", backgroundColor:"#ffffff"
		}
               
        standardTile("channelDown", "device.channelDown") {
			state "channelDown", label:'CH DN', action: "channelDown", backgroundColor:"#ffffff"
		}
        
        
        standardTile("inputPooq", "device.inputPooq") {
			state "inputPooq", label:'pooq', action: "goPooq", backgroundColor:"#ffffff"
		}
        standardTile("inputTving", "device.inputTving") {
			state "inputTving", label:'Tving', action: "goTving", backgroundColor:"#ffffff"
		}
        standardTile("inputWatcha", "device.inputWatcha") {
			state "inputWatcha", label:'Watcha', action: "goWatcha", backgroundColor:"#ffffff"
		}
        
        standardTile("inputPhoto", "device.inputPhoto") {
			state "inputPhoto", label:'Photo', action: "goPhoto", backgroundColor:"#ffffff"
		}
        standardTile("inputWeb", "device.inputWeb") {
			state "inputWeb", label:'Web', action: "goWeb", backgroundColor:"#ffffff"
		}
        
        standardTile("play", "device.play") {
			state "play", label:'Play', action: "mediaPlay", backgroundColor:"#ffffff"
		}
        standardTile("pause", "device.pause") {
			state "pause", label:'Pause', action: "mediaPause", backgroundColor:"#ffffff"
		}
        standardTile("stop", "device.stop") {
			state "stop", label:'Stop', action: "mediaStop", backgroundColor:"#ffffff"
		}
        
        valueTile("timer_label", "device.leftTime", decoration: "flat", width: 2, height: 1) {
            state "default", label:'Set Timer\n${currentValue}'
        }
        
        controlTile("time", "device.timeRemaining", "slider", height: 1, width: 1, range:"(0..120)") {
	    	state "time", action:"setTimeRemaining"
		}
        
        standardTile("tiemr0", "device.timeRemaining") {
			state "default", label: "OFF", action: "stop", icon:"st.Health & Wellness.health7", backgroundColor:"#c7bbc9"
		}
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
	log.debug "Update >> ${data.key} >> ${data.data}"
	if(data.cmd == "notify"){
        switch(data.key){
        case "power":
			sendEvent(name:"switch", value: data.data )
            break; 
        case "volume":
			sendEvent(name:"level", value: data.data as int )
            break;
        case "mute":
			sendEvent(name:"mute", value: data.data == "true" ? "mute" : "unmuted" )
            break;
        case "input":
        	updateInput(data.data)
            break;
        case "channelNumber":
			sendEvent(name:"channelNumber", value: data.data )
            break;
        case "channelName":
			sendEvent(name:"channelName", value: data.data )
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

def setLevel(level){
    makeCommand("volume", level as int)
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
	def myhubAction = new physicalgraph.device.HubAction(options, null, [callback: _callback])
    sendHubCommand(myhubAction)
}
