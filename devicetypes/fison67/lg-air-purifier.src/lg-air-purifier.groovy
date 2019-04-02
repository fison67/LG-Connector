/**
 *  LG Air PuriFier(v.0.0.1)
 *
 * MIT License
 *
 * Copyright (c) 2019 fison67@nate.com
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
import groovy.transform.Field

@Field
OPERATION_VALUE = [
    14: [val: "@AP_MAIN_MID_OPMODE_CIRCULATOR_CLEAN_W", str: ["EN": "Clean Booster", "KR": "클린부스터"]],
    15: [val: "@AP_MAIN_MID_OPMODE_BABY_CARE_W", str: ["EN": "Baby Care", "KR": "싱글청정"]],
    16: [val: "@AP_MAIN_MID_OPMODE_DUAL_CLEAN_W", str: ["EN": "Dual Clean", "KR": "듀얼청정"]],
    17: [val: "@AP_MAIN_MID_OPMODE_AUTO_W", str: ["EN": "Auto", "KR": "오토모드"]]
]

@Field 
CIRCULATOR_VALUE = [
    3: [val:"@AP_MAIN_MID_CIRCULATORSTRENGTH_LOW_W", str: ["EN": "Low", "KR": "약"]],
    5: [val:"@AP_MAIN_MID_CIRCULATORSTRENGTH_MID_W", str: ["EN": "Mid", "KR": "중"]],
    7: [val:"@AP_MAIN_MID_CIRCULATORSTRENGTH_HIGH_W", str: ["EN": "High", "KR": "강"]],
    8: [val:"@AP_MAIN_MID_CIRCULATORSTRENGTH_POWER_W", str: ["EN": "Power", "KR": "터보"]],
    9: [val:"@AP_MAIN_MID_CIRCULATORSTRENGTH_AUTO_W", str: ["EN": "Auto", "KR": "오토"]]
]

@Field 
WIND_VALUE = [
    3: [val:"@AP_MAIN_MID_WINDSTRENGTH_LOW_W", str: ["EN": "Low", "KR": "약"]],
    5: [val:"@AP_MAIN_MID_WINDSTRENGTH_MID_W", str: ["EN": "Mid", "KR": "중"]],
    7: [val:"@AP_MAIN_MID_WINDSTRENGTH_HIGH_W", str: ["EN": "High", "KR": "강"]],
    8: [val:"@AP_MAIN_MID_WINDSTRENGTH_POWER_W", str: ["EN": "Power", "KR": "터보"]],
    9: [val:"@AP_MAIN_MID_WINDSTRENGTH_AUTO_W", str: ["EN": "Auto", "KR": "오토"]]
]

metadata {
	definition (name: "LG Air PuriFier", namespace: "fison67", author: "fison67") {
        capability "Switch"
        capability "Switch Level"
        capability "Temperature Measurement"
        capability "Relative Humidity Measurement"
        capability "Dust Sensor"
        capability "Refresh"
        
        attribute "pm1", "number"
        attribute "mode", "string"
        attribute "wind", "string"
        attribute "circulator", "string"
        attribute "airRemoval", "string"
        attribute "circulatorDirection", "string"
        attribute "totalAirPolution", "number"
        
        command "airRemovalOff"
        command "airRemovalOn"
        command "circulatorDirectionOff"
        command "circulatorDirectionOn"
        
        command "setWind", ["number"]
        command "wind1"
        command "wind2"
        command "wind3"
        command "wind4"
        command "wind5"
        
        command "setMode", ["number"]
        command "mode1"
        command "mode2"
        command "mode3"
        command "mode4"
        command "mode5"
        
        command "setCir", ["number"]
        command "cur1"
        command "cur2"
        command "cur3"
        command "cur4"
        command "cur5"
	}

	simulator {
	}
    
	preferences {
        input name: "language", title:"Select a language" , type: "enum", required: true, options: ["EN", "KR"], defaultValue: "KR", description:"Language for DTH"
        
        input name: "mode1", title:"Mode#1 Type" , type: "number", required: false, defaultValue: 14
        input name: "mode2", title:"Mode#2 Type" , type: "number", required: false, defaultValue: 15
        input name: "mode3", title:"Mode#3 Type" , type: "number", required: false, defaultValue: 16
        input name: "mode4", title:"Mode#4 Type" , type: "number", required: false, defaultValue: 17
        
        input name: "wind1", title:"Wind#1 Type" , type: "number", required: false, defaultValue: 3
        input name: "wind2", title:"Wind#2 Type" , type: "number", required: false, defaultValue: 5
        input name: "wind3", title:"Wind#3 Type" , type: "number", required: false, defaultValue: 7
        input name: "wind4", title:"Wind#4 Type" , type: "number", required: false, defaultValue: 8
        input name: "wind5", title:"Wind#5 Type" , type: "number", required: false, defaultValue: 9
        
        input name: "cir1", title:"Circulate#1 Type" , type: "number", required: false, defaultValue: 3
        input name: "cir2", title:"Circulate#2 Type" , type: "number", required: false, defaultValue: 5
        input name: "cir3", title:"Circulate#3 Type" , type: "number", required: false, defaultValue: 7
        input name: "cir4", title:"Circulate#4 Type" , type: "number", required: false, defaultValue: 8
        input name: "cir5", title:"Circulate#5 Type" , type: "number", required: false, defaultValue: 9
        
	}

	tiles(scale: 2) {
		
        multiAttributeTile(name:"switch", type: "generic", width: 6, height: 2){
            tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
              attributeState "on", label:'${name}', action:"switch.off",  backgroundColor:"#00a0dc", nextState:"turningOff", icon:"https://github.com/fison67/LG-Connector/blob/master/icons/lg-air-half-on.png?raw=true"
                attributeState "off", label:'${name}', action:"switch.on", backgroundColor:"#ffffff", nextState:"turningOn", icon:"https://github.com/fison67/LG-Connector/blob/master/icons/lg-air-half-off.png?raw=true"
                
                attributeState "turningOn", label:'${name}', action:"switch.off", backgroundColor:"#00a0dc", nextState:"turningOff", icon:"https://github.com/fison67/LG-Connector/blob/master/icons/lg-air-half-off.png?raw=true"
                attributeState "turningOff", label:'${name}', action:"switch.on", backgroundColor:"#ffffff", nextState:"turningOn", icon:"https://github.com/fison67/LG-Connector/blob/master/icons/lg-air-half-on.png?raw=true"
			}
            
			tileAttribute("device.lastCheckin", key: "SECONDARY_CONTROL") {
    			attributeState("default", label:'Updated: ${currentValue}')
            }
		}
        controlTile("humidityControl", "device.level", "slider", range:"(0..100)", height: 1, width: 2) {
            state "level", action:"setLevel"
        }
        valueTile("mode", "device.mode", decoration: "flat", width: 4, height: 1) {
            state "default", label:'${currentValue}'
        }
        
        standardTile("circulatorLabel", "device.circulatorLabel", decoration: "flat", height: 1, width: 2) {
			state "default", label: "Circulator"
		}
        valueTile("circulator", "device.circulator", decoration: "flat", width: 4, height: 1) {
            state "default", label:'${currentValue}'
        }
        standardTile("windLabel", "device.windLabel", decoration: "flat", height: 1, width: 2) {
			state "default", label: "Wind"
		}
        valueTile("wind", "device.wind", decoration: "flat", width: 4, height: 1) {
            state "default", label:'${currentValue}'
        }
        
        standardTile("airPolutionLabel", "device.airPolutionLabel", decoration: "flat", height: 1, width: 2) {
			state "default", label: "AirPolution"
		}
        valueTile("totalAirPolution", "device.totalAirPolution", decoration: "flat", width: 4, height: 1) {
            state "default", label:'${currentValue}°'
        }
        standardTile("tempLabel", "device.tempLabel", decoration: "flat", height: 1, width: 2) {
			state "default", label: "Temperature"
		}
        valueTile("temperature", "device.temperature", decoration: "flat", width: 4, height: 1) {
            state "default", label:'${currentValue}°'
        }
        standardTile("humidityLabel", "device.humidityLabel", decoration: "flat", height: 1, width: 2) {
			state "default", label: "Humidity"
		}
        valueTile("humidity", "device.humidity", decoration: "flat", width: 4, height: 1) {
            state "default", label:'${currentValue}%'
        }
        standardTile("pm1Label", "device.pm1Label", decoration: "flat", height: 1, width: 2) {
			state "default", label: "PM1"
		}
        valueTile("pm1", "device.pm1", decoration: "flat", width: 4, height: 1) {
            state "default", label:'${currentValue}'
        }
        standardTile("pm25Label", "device.pm25Label", decoration: "flat", height: 1, width: 2) {
			state "default", label: "PM2.5"
		}
        valueTile("fineDustLevel", "device.fineDustLevel", decoration: "flat", width: 4, height: 1) {
            state "default", label:'${currentValue}'
        }
        standardTile("pm10Label", "device.pm10Label", decoration: "flat", height: 1, width: 2) {
			state "default", label: "PM10"
		}
        valueTile("dustLevel", "device.dustLevel", decoration: "flat", width: 4, height: 1) {
            state "default", label:'${currentValue}'
        }
        standardTile("airRemoval", "device.airRemoval", inactiveLabel: false, width: 1, height: 1, canChangeIcon: true) {
            state "on", label:'Air ${name}', action:"airRemovalOff", backgroundColor:"#00a0dc", nextState:"turningOff"
            state "off", label:'Air ${name}', action:"airRemovalOn", backgroundColor:"#ffffff", nextState:"turningOn"
             
        	state "turningOn", label:'....', action:"airRemovalOff", backgroundColor:"#00a0dc", nextState:"turningOff"
            state "turningOff", label:'....', action:"airRemovalOn", backgroundColor:"#ffffff", nextState:"turningOn"
        }
        standardTile("circulatorDirection", "device.circulatorDirection", inactiveLabel: false, width: 1, height: 1, canChangeIcon: true) {
            state "on", label:'Cir ${name}', action:"circulatorDirectionOff", backgroundColor:"#00a0dc", nextState:"turningOff"
            state "off", label:'Cir ${name}', action:"circulatorDirectionOn", backgroundColor:"#ffffff", nextState:"turningOn"
             
        	state "turningOn", label:'....', action:"circulatorDirectionOff", backgroundColor:"#00a0dc", nextState:"turningOff"
            state "turningOff", label:'....', action:"circulatorDirectionOn", backgroundColor:"#ffffff", nextState:"turningOn"
        }
        
        valueTile("mode1", "device.mode1", decoration: "flat", height: 1, width: 1) {
			state "default", label:'${currentValue}', action: "mode1"
		}
        valueTile("mode2", "device.mode2", decoration: "flat", height: 1, width: 1) {
			state "default", label:'${currentValue}', action: "mode2"
		}
        valueTile("mode3", "device.mode3", decoration: "flat", height: 1, width: 1) {
			state "default", label:'${currentValue}', action: "mode3"
		}
        valueTile("mode4", "device.mode4", decoration: "flat", height: 1, width: 1) {
			state "default", label:'${currentValue}', action: "mode4"
		}
        
        standardTile("windCLabel", "device.windCLabel", decoration: "flat", height: 1, width: 1) {
			state "default", label: "Wind"
		}
        valueTile("wind1", "device.wind1", decoration: "flat", height: 1, width: 1) {
			state "default", label:'${currentValue}', action: "wind1"
		}
        valueTile("wind2", "device.wind2", decoration: "flat", height: 1, width: 1) {
			state "default", label:'${currentValue}', action: "wind2"
		}
        valueTile("wind3", "device.wind3", decoration: "flat", height: 1, width: 1) {
			state "default", label:'${currentValue}', action: "wind3"
		}
        valueTile("wind4", "device.wind4", decoration: "flat", height: 1, width: 1) {
			state "default", label:'${currentValue}', action: "wind4"
		}
        valueTile("wind5", "device.wind5", decoration: "flat", height: 1, width: 1) {
			state "default", label:'${currentValue}', action: "wind5"
		}
        
        standardTile("cirLabel", "device.cirLabel", decoration: "flat", height: 1, width: 1) {
			state "default", label: "Cir"
		}
        valueTile("cir1", "device.cir1", decoration: "flat", height: 1, width: 1) {
			state "default", label:'${currentValue}', action: "cir1"
		}
        valueTile("cir2", "device.cir2", decoration: "flat", height: 1, width: 1) {
			state "default", label:'${currentValue}', action: "cir2"
		}
        valueTile("cir3", "device.cir3", decoration: "flat", height: 1, width: 1) {
			state "default", label:'${currentValue}', action: "cir3"
		}
        valueTile("cir4", "device.cir4", decoration: "flat", height: 1, width: 1) {
			state "default", label:'${currentValue}', action: "cir4"
		}
        valueTile("cir5", "device.cir5", decoration: "flat", height: 1, width: 1) {
			state "default", label:'${currentValue}', action: "cir5"
		}
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

def updated() {
	log.debug "updated"
	sendEvent(name: "mode1", value: OPERATION_VALUE[mode1]["str"][language])
	sendEvent(name: "mode2", value: OPERATION_VALUE[mode2]["str"][language])
	sendEvent(name: "mode3", value: OPERATION_VALUE[mode3]["str"][language])
	sendEvent(name: "mode4", value: OPERATION_VALUE[mode4]["str"][language])
    
    log.debug WIND_VALUE[wind1]["str"][language]
	sendEvent(name: "wind1", value: WIND_VALUE[wind1]["str"][language])
	sendEvent(name: "wind2", value: WIND_VALUE[wind2]["str"][language])
	sendEvent(name: "wind3", value: WIND_VALUE[wind3]["str"][language])
	sendEvent(name: "wind4", value: WIND_VALUE[wind4]["str"][language])
	sendEvent(name: "wind5", value: WIND_VALUE[wind5]["str"][language])
    
	sendEvent(name: "cir1", value: CIRCULATOR_VALUE[cir1]["str"][language])
	sendEvent(name: "cir2", value: CIRCULATOR_VALUE[cir2]["str"][language])
	sendEvent(name: "cir3", value: CIRCULATOR_VALUE[cir3]["str"][language])
	sendEvent(name: "cir4", value: CIRCULATOR_VALUE[cir4]["str"][language])
	sendEvent(name: "cir5", value: CIRCULATOR_VALUE[cir5]["str"][language])
}

def setInfo(String app_url, String address) {
	log.debug "${app_url}, ${address}"
	state.app_url = app_url
    state.id = address
}

def setData(dataList){
	for(data in dataList){
        state[data.id] = data.code
    }
}

def setStatus(data){
//	log.debug "Update >> ${data.key} >> ${data.data}"
    def jsonObj = new JsonSlurper().parseText(data.data)
    
    if(jsonObj.Operation){
    	def power = jsonObj.Operation.value == "0" ? "off" : "on"
    	sendEvent(name: "switch", value: power)
        
    //    if(power == "on"){
            if(jsonObj.SensorPM10){
                sendEvent(name: "dustLevel", value: jsonObj.SensorPM10.value as int, displayText: "PM10 is " + jsonObj.SensorPM10.value)
            }
            if(jsonObj.SensorPM2){
                sendEvent(name: "fineDustLevel", value: jsonObj.SensorPM2.value as int, displayText: "PM2 is " + jsonObj.SensorPM2.value)
            }
            if(jsonObj.SensorPM1){
                sendEvent(name: "pm1", value: jsonObj.SensorPM1.value as int, displayText: "PM1 is " + jsonObj.SensorPM1.value)
            }
    //    }

    }
    if(jsonObj.TempCur){
    	sendEvent(name: "temperature", value: jsonObj.TempCur.value as int, displayed: false)
    }
    if(jsonObj.SensorHumidity){
    	sendEvent(name:"humidity", value: jsonObj.SensorHumidity.value as int)
    }
    if(jsonObj.OpMode){
    	sendEvent(name: "mode", value: OPERATION_VALUE[jsonObj.OpMode.value as int]["str"][language])
    }
    
    if(jsonObj.HumidityCfg){
    	sendEvent(name: "level", value: jsonObj.HumidityCfg.value as int)
    }
    if(jsonObj.TotalAirPolution){
    	sendEvent(name: "totalAirPolution", value: jsonObj.TotalAirPolution.value as int)
    }
    if(jsonObj.WindStrength){
    	sendEvent(name: "wind", value: WIND_VALUE[jsonObj.WindStrength.value as int]["str"][language])
    }
    if(jsonObj.CirculateStrength){
    	sendEvent(name: "circulator", value: CIRCULATOR_VALUE[jsonObj.CirculateStrength.value as int]["str"][language])
    }
    if(jsonObj.CirculateDir){
    	sendEvent(name: "circulatorDirection", value: (jsonObj.CirculateDir.value == "0" ? "off" : "on"))
    }
    if(jsonObj.AirRemoval){
    	sendEvent(name: "airRemoval", value: (jsonObj.AirRemoval.value == "0" ? "off" : "on"))
    }
    
    updateLastTime();
}

def updateLastTime(){
	def now = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)
    sendEvent(name: "lastCheckin", value: now, displayed:false)
}

def setLevel(level){
	makeCommand("SetHumidityCfg", '{"HumidityCfg":"' + level + '"}')
}

def on(){
	makeCommand("SetOperation", "Start")
}

def off(){
	makeCommand("SetOperation", "Stop")
}

def setCir(val){
	makeCommand("SetCirculateStrength", '{"CirculateStrength":"' + val + '"}')
}

def cir1(){
    setCir(cir1)
}

def cir2(){
    setCir(cir2)
}

def cir3(){
    setCir(cir3)
}

def cir4(){
    setCir(cir4)
}

def cir5(){
    setCir(cir5)
}

def setWind(val){
	makeCommand("SetWindStrength", '{"WindStrength":"' + val + '"}')
}

def wind1(){
    setWind(wind1)
}

def wind2(){
    setWind(wind2)
}

def wind3(){
    setWind(wind3)
}

def wind4(){
    setWind(wind4)
}

def wind5(){
    setWind(wind5)
}


def setMode(val){
	makeCommand("SetOpMode", '{"OpMode":"' + val + '"}')
}

def mode1(){
    setMode(mode1)
}

def mode2(){
    setMode(mode2)
}

def mode3(){
    setMode(mode3)
}

def mode4(){
    setMode(mode4)
}

def airRemovalOn(){
	makeCommand("SetAirRemoval", '{"AirRemoval":"1"}')
}

def airRemovalOff(){
	makeCommand("SetAirRemoval", '{"AirRemoval":"0"}')
}

def circulatorDirectionOn(){
	makeCommand("SetCirculateDir", '{"CirculateDir":"1"}')
}

def circulatorDirectionOff(){
	makeCommand("SetCirculateDir", '{"CirculateDir":"0"}')
}

def control(cmd, value){
	makeCommand(cmd, value)
}

def makeCommand(command, value){
    def body = [
        "id": state.id,
        "command": command,
        "value": value
    ]
    def options = _makeCommand(body)
    sendCommand(options, null)
}

def _makeCommand(body){
	def options = [
     	"method": "POST",
        "path": "/devices/control",
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
