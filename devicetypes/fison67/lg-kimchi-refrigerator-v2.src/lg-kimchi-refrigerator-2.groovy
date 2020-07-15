/**
 *  LG Kimchi Refrigerator v2(v.0.0.1)
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

metadata {
	definition (name: "LG Kimchi Refrigerator v2", namespace: "fison67", author: "fison67", mnmn:"SmartThings", vid: "generic-contact") {
        capability "Sensor"
        capability "Contact Sensor"
        
        command "setStatus"
        
        attribute "room1Temp", "string"
        attribute "room2Temp", "string"
        attribute "room3Temp", "string"
        attribute "room4Temp", "string"
        attribute "lastOpen", "string"
	}

	simulator {
	}
    
	preferences {
        input name: "language", title:"Select a language" , type: "enum", required: true, options: ["EN", "KR"], defaultValue: "KR", description:"Language for DTH"
	}

	tiles(scale: 2) {
		
        multiAttributeTile(name:"contact", type: "generic", width: 6, height: 2){
            tileAttribute ("device.contact", key: "PRIMARY_CONTROL") {
               	attributeState "open", label:'${name}', icon:"https://github.com/fison67/LG-Connector/blob/master/icons/lg-refrigerator.png?raw=true", backgroundColor:"#e86d13"
            	attributeState "closed", label:'${name}', icon:"https://github.com/fison67/LG-Connector/blob/master/icons/lg-refrigerator.png?raw=true", backgroundColor:"#00a0dc"
			}
            
			tileAttribute("device.lastCheckin", key: "SECONDARY_CONTROL") {
    			attributeState("default", label:'Updated: ${currentValue}')
            }
		}
       
       
        valueTile("room1Temp_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'Temp1'
        }
        valueTile("room1Temp", "device.room1Temp", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }
       
        valueTile("room2Temp_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'Temp2'
        }
        valueTile("room2Temp", "device.room2Temp", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }
        
        valueTile("room3Temp_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'Temp3'
        }
        valueTile("room3Temp", "device.room3Temp", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }
        
        valueTile("room4Temp_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'Temp4'
        }
        valueTile("room4Temp", "device.room4Temp", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }
      
        valueTile("lastOpen_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'Last\nOpen'
        }
        valueTile("lastOpen", "device.lastOpen", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }
        
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

def updated() {}

def setInfo(String app_url, String address) {
	log.debug "${app_url}, ${address}"
	state.app_url = app_url
    state.id = address
}

def getTemp1String(status){
	def str = ""
	switch(status){
    case 0:
    	str = (language == "KR" ? "김치 중" : "Kimchi Middle")
    	break
    case 1:
    	str = (language == "KR" ? "김치 강" : "Kimchi Strong")
    	break
    case 2:
    	str = (language == "KR" ? "김치 약" : "Kimchi Weak")
    	break
    case 3:
    	str = (language == "KR" ? "냉장고 중" : "Fridge Middle")
    	break
    case 4:
    	str = (language == "KR" ? "냉장고 강" : "Fridge Strong")
    	break
    case 5:
    	str = (language == "KR" ? "냉장고 약" : "Fridge Weak")
    	break
    case 6:
    	str = (language == "KR" ? "냉동고" : "Freezer")
    	break
    case 7:
    	str = (language == "KR" ? "Aging" : "Aging")
    	break
    case 8:
    	str = (language == "KR" ? "김치 중 Aging" : "Kimchi Middle Aging")
    	break
    case 9:
    	str = (language == "KR" ? "Top Off?" : "Top Off")
    	break
    case 255:
    	str = (language == "KR" ? "Ignore" : "Ignore")
    	break
    }
    return str
}

def getTemp3String(status){
	def str = ""
	switch(status){
    case 0:
    	str = (language == "KR" ? "김치 중" : "Kimchi Middle")
    	break
    case 1:
    	str = (language == "KR" ? "김치 강" : "Kimchi Strong")
    	break
    case 2:
    	str = (language == "KR" ? "김치 약" : "Kimchi Weak")
    	break
    case 3:
    	str = (language == "KR" ? "야채과일 중" : "Vegi Fruit Middle")
    	break
    case 4:
    	str = (language == "KR" ? "야채과일 강" : "Vegi Fruit Strong")
    	break
    case 5:
    	str = (language == "KR" ? "야채과일 약" : "Vegi Fruit Weak")
    	break
    case 6:
    	str = (language == "KR" ? "고기 생선" : "Meat Fish")
    	break
    case 7:
    	str = (language == "KR" ? "Lacto 김치" : "Lacto Kimchi")
    	break
    case 8:
    	str = (language == "KR" ? "Lacto 김치 1" : "Lacto Kimchi 1")
    	break
    case 9:
    	str = (language == "KR" ? "Lacto 김치 2" : "Lacto Kimchi 2")
    	break
    case 10:
    	str = (language == "KR" ? "Lacto 김치 3" : "Lacto Kimchi 3")
    	break
    case 11:
    	str = (language == "KR" ? "Aging" : "Aging")
    	break
    case 12:
    	str = (language == "KR" ? "김치 중 Aging" : "Kimchi Middle Aging")
    	break
    case 13:
    	str = (language == "KR" ? "OFF" : "OFF")
    	break
    case 255:
    	str = (language == "KR" ? "Ignore" : "Ignore")
    	break
    }
    return str
}

def getTemp4String(status){
	def str = ""
	switch(status){
    case 0:
    	str = (language == "KR" ? "김치 중" : "Kimchi Middle")
    	break
    case 1:
    	str = (language == "KR" ? "김치 강" : "Kimchi Strong")
    	break
    case 2:
    	str = (language == "KR" ? "김치 약" : "Kimchi Weak")
    	break
    case 3:
    	str = (language == "KR" ? "야채과일 중" : "Vegi Fruit Middle")
    	break
    case 4:
    	str = (language == "KR" ? "야채과일 강" : "Vegi Fruit Strong")
    	break
    case 5:
    	str = (language == "KR" ? "야채과일 약" : "Vegi Fruit Weak")
    	break
    case 6:
    	str = (language == "KR" ? "쌀 곡물" : "Rice Grain")
    	break
    case 7:
    	str = (language == "KR" ? "Bought 김치" : "Bought Kimchi")
    	break
    case 8:
    	str = (language == "KR" ? "Long Storage" : "Long Storage")
    	break
    case 9:
    	str = (language == "KR" ? "OFF" : "OFF")
    	break
    case 255:
    	str = (language == "KR" ? "Ignore" : "Ignore")
    	break
    }
    return str
}



def setStatus(data){
	log.debug "Update >> ${data.key} >> ${data.data}"
    def jsonObj = new JsonSlurper().parseText(data.data)
    
    if(jsonObj.data.state.reported != null){
    	def report = jsonObj.data.state.reported
        
        if(report["kmcState"] != null){
        	def kmcState = report["kmcState"]
            
            if(kmcState["atLeastOneDoorOpen"] != null){
        		sendEvent(name:"contact", value: (kmcState["atLeastOneDoorOpen"] == "CLOSE" ? "closed" : "open"))
                if(kmcState["atLeastOneDoorOpen"] == "OPEN"){
                    sendEvent(name:"lastOpen", value: new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone))
                }
            }
            
            if(kmcState["room1Temp"] != null){
        		sendEvent(name:"room1Temp", value: getTemp1String(kmcState["room1Temp"]))
            }
            if(kmcState["room2Temp"] != null){
        		sendEvent(name:"room2Temp", value: getTemp1String(kmcState["room2Temp"]))
            }
            if(kmcState["room3Temp"] != null){
        		sendEvent(name:"room3Temp", value: getTemp3String(kmcState["room3Temp"]))
            }
            if(kmcState["room4Temp"] != null){
        		sendEvent(name:"room4Temp", value: getTemp4String(kmcState["room4Temp"]))
            }
        }
        
    }
   
    updateLastTime();
}

def updateLastTime(){
	def now = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)
    sendEvent(name: "lastCheckin", value: now, displayed:false)
}
