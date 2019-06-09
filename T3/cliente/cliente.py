
import requests 
from xml.etree import ElementTree as et

# api-endpoint 
URL = "http://localhost:8080/getTransfer/server/cotacao"
  
# defining a params dict for the parameters to be sent to the API 
#PARAMS = {'address':brasil} 
PARAMS = ''
  
# sending get request and saving the response as response object 
r = requests.get(url = URL, params = PARAMS) 

#XML parsers
data = et.fromstring(r.content)

print(r)
print(data[0].text)

