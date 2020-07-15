/**
 *  LG Air PuriFier v2(v.0.0.1)
 *
 * MIT License
 *
 * Copyright (c) 2020 fison67@nate.com
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
    0: [val: "@AC_MAIN_OPERATION_MODE_COOL_W", str: ["EN": "Cool", "KR": "Cool"]],
	1: [val: "@AC_MAIN_OPERATION_MODE_DRY_W", str: ["EN": "Dry", "KR": "Dry"]],
    2: [val: "@AC_MAIN_OPERATION_MODE_FAN_W", str: ["EN": "Fan", "KR": "Fan"]],
    3: [val: "@AC_MAIN_OPERATION_MODE_AI_W", str: ["EN": "AI", "KR": "AI"]],
    4: [val: "@AC_MAIN_OPERATION_MODE_HEAT_W", str: ["EN": "Heat", "KR": "Heat"]],
    5: [val: "@AC_MAIN_OPERATION_MODE_AIRCLEAN_W", str: ["EN": "Air Clean", "KR": "공기청정"]],
    6: [val: "@AC_MAIN_OPERATION_MODE_ACO_W", str: ["EN": "Aco", "KR": "Aco"]],
    7: [val: "@AC_MAIN_OPERATION_MODE_AROMA_W", str: ["EN": "Aroma", "KR": "아로마"]],
    8: [val: "@AC_MAIN_OPERATION_MODE_ENERGY_SAVING_W", str: ["EN": "Energy Saving", "KR": "Energy saving"]],
    9: [val: "@AP_MAIN_MID_OPMODE_CLEAN_W", str: ["EN": "Clean", "KR": "청정"]],
    10: [val: "@AP_MAIN_MID_OPMODE_SLEEP_W", str: ["EN": "Sleep", "KR": "취침"]],
    11: [val: "@AP_MAIN_MID_OPMODE_SILENT_W", str: ["EN": "Silent", "KR": "정음청정"]],
    12: [val: "@AP_MAIN_MID_OPMODE_HUMIDITY_W", str: ["EN": "Humidity", "KR": "가습"]],
    13: [val: "@AP_MAIN_MID_OPMODE_CIRCULATOR_CLEAN_W", str: ["EN": "Clean Booster", "KR": "클린부스터"]],
    14: [val: "@AP_MAIN_MID_OPMODE_BABY_CARE_W", str: ["EN": "Baby Care", "KR": "싱글청정"]],
    15: [val: "@AP_MAIN_MID_OPMODE_DUAL_CLEAN_W", str: ["EN": "Dual Clean", "KR": "듀얼청정"]],
    16: [val: "@AP_MAIN_MID_OPMODE_AUTO_W", str: ["EN": "Auto", "KR": "오토모드"]]
]

@Field 
WIND_VALUE = [
    0: [val:"@AP_MAIN_MID_WINDSTRENGTH_LOWST_LOW_W", str: ["EN": "Lowst Low", "KR": "약약약"]],
    1: [val:"@AP_MAIN_MID_WINDSTRENGTH_LOWST_W", str: ["EN": "Lowst", "KR": "약약"]],
    2: [val:"@AP_MAIN_MID_WINDSTRENGTH_LOW_W", str: ["EN": "Low", "KR": "약"]],
    3: [val:"@AP_MAIN_MID_WINDSTRENGTH_LOW_MID_W", str: ["EN": "Low Mid", "KR": "약중"]],
    4: [val:"@AP_MAIN_MID_WINDSTRENGTH_MID_W", str: ["EN": "Mid", "KR": "중"]],
    5: [val:"@AP_MAIN_MID_WINDSTRENGTH_MID_HIGH_W", str: ["EN": "Mid High", "KR": "중상"]],
    6: [val:"@AP_MAIN_MID_WINDSTRENGTH_HIGH_W", str: ["EN": "High", "KR": "상"]],
    7: [val:"@AP_MAIN_MID_WINDSTRENGTH_POWER_W", str: ["EN": "Power", "KR": "파워"]],
    8: [val:"@AP_MAIN_MID_WINDSTRENGTH_AUTO_W", str: ["EN": "Auto", "KR": "오토"]],
    9: [val:"@AP_MAIN_MID_WINDSTRENGTH_LONGPOWWER_W", str: ["EN": "Long POwer", "KR": "중파워"]],
    10: [val:"@AP_MAIN_MID_WINDSTRENGTH_SHOWER_W", str: ["EN": "Shower", "KR": "Shower"]],
    11: [val:"@AP_MAIN_MID_WINDSTRENGTH_FOREST_W", str: ["EN": "Forest", "KR": "Forest"]],
    12: [val:"@AP_MAIN_MID_WINDSTRENGTH_TURBO_W", str: ["EN": "Turbo", "KR": "터보"]],
    13: [val:"@AP_MAIN_MID_WINDSTRENGTH_FASTWIND_W", str: ["EN": "Fast Wind", "KR": "Fast Wind"]]
]

metadata {
	definition (name: "LG Air PuriFier v2", namespace: "fison67", author: "fison67") {
        capability "Switch"
        capability "Switch Level"
        capability "Temperature Measurement"
        capability "Relative Humidity Measurement"
        capability "Dust Sensor"
        capability "Refresh"
        
        attribute "pm1", "number"
        attribute "mode", "string"
        attribute "wind", "string"
        attribute "airRemoval", "string"
        
        command "airRemovalOff"
        command "airRemovalOn"
        
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
	}

	simulator {
	}
    
	preferences {
        input name: "language", title:"Select a language" , type: "enum", required: true, options: ["EN", "KR"], defaultValue: "KR", description:"Language for DTH"
        
        input name: "mode1", title:"Mode#1 Type" , type: "number", required: false, defaultValue: 13
        input name: "mode2", title:"Mode#2 Type" , type: "number", required: false, defaultValue: 14
        input name: "mode3", title:"Mode#3 Type" , type: "number", required: false, defaultValue: 15
        input name: "mode4", title:"Mode#4 Type" , type: "number", required: false, defaultValue: 16
        
        input name: "wind1", title:"Wind#1 Type" , type: "number", required: false, defaultValue: 3
        input name: "wind2", title:"Wind#2 Type" , type: "number", required: false, defaultValue: 5
        input name: "wind3", title:"Wind#3 Type" , type: "number", required: false, defaultValue: 7
        input name: "wind4", title:"Wind#4 Type" , type: "number", required: false, defaultValue: 8
        input name: "wind5", title:"Wind#5 Type" , type: "number", required: false, defaultValue: 9
        
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
            state "default", label:'${currentValue}'
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
    
	sendEvent(name: "wind1", value: WIND_VALUE[wind1]["str"][language])
	sendEvent(name: "wind2", value: WIND_VALUE[wind2]["str"][language])
	sendEvent(name: "wind3", value: WIND_VALUE[wind3]["str"][language])
	sendEvent(name: "wind4", value: WIND_VALUE[wind4]["str"][language])
	sendEvent(name: "wind5", value: WIND_VALUE[wind5]["str"][language])
}

def setInfo(String app_url, String address) {
	log.debug "${app_url}, ${address}"
	state.app_url = app_url
    state.id = address
}

def setStatus(data){
    log.debug "Update >> ${data.key} >> ${data.data}"
    def jsonObj = new JsonSlurper().parseText(data.data)
    
    if(jsonObj.data.state.reported != null){
    	def report = jsonObj.data.state.reported
        
        if(report["airState.operation"] != null){
        	sendEvent(name: "switch", value: (report["airState.operation"] == 0 ? "off" : "on"))
        }
        if(report["airState.opMode"] != null){
        	sendEvent(name: "mode", value: OPERATION_VALUE[report["airState.opMode"]]["str"][language])
        }
        
        if(report["airState.windStrength"] != null){
        	sendEvent(name: "wind", value:  WIND_VALUE[report["airState.windStrength"]]["str"][language] )
        }
        if(report["airState.miscFuncState.airRemoval"] != null){
        	sendEvent(name: "airRemoval", value: report["airState.miscFuncState.airRemoval"] == 0 ? "off" : "on")
        }
        
        
        
        if(report["airState.humidity.current"] != null){
        	sendEvent(name: "humidity", value: report["airState.humidity.current"])
        }
        if(report["airState.tempState.current"] != null){
        	sendEvent(name: "temperature", value: report["airState.tempState.current"])
        }
        
        if(report["airState.quality.PM1"] != null){
        	sendEvent(name: "pm1", value: report["airState.quality.PM1"])
        }
        if(report["airState.quality.PM10"] != null){
        	sendEvent(name: "dustLevel", value: report["airState.quality.PM10"])
        }
        if(report["airState.quality.PM2"] != null){
        	sendEvent(name: "fineDustLevel", value: report["airState.quality.PM2"])
        }
    }
    
    updateLastTime();
}

def updateLastTime(){
	def now = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)
    sendEvent(name: "lastCheckin", value: now, displayed:false)
}

def setLevel(level){

}

def on(){
	makeCommand('', '{"command":"Operation","ctrlKey":"basicCtrl","dataKey":"airState.operation","dataValue":1}')
}

def off(){
	makeCommand('', '{"command":"Operation","ctrlKey":"basicCtrl","dataKey":"airState.operation","dataValue":0}')
}

def setWind(val){
	makeCommand('', '{"command":"Set","ctrlKey":"basicCtrl","dataKey":"airState.windStrength","dataValue":' + val + '}')
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
	makeCommand('', '{"command":"Set","ctrlKey":"basicCtrl","dataKey":"airState.opMode","dataValue":' + val + '}')
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
	makeCommand('', '{"command":"Set","ctrlKey":"basicCtrl","dataKey":"airState.airRemoval","dataValue":1}')
}

def airRemovalOff(){
	makeCommand('', '{"command":"Set","ctrlKey":"basicCtrl","dataKey":"airState.airRemoval","dataValue":0}')
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
        "path": "/devices/control2",
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
