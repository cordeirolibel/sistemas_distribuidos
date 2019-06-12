#!/usr/bin/env python
# -*- coding: utf-8 -*-

import requests 
from xml.etree import ElementTree as et
import os

# api-endpoint
URL_COTACAO = "http://localhost:8080/getTransfer/server/cotacao"
URL_RESERVA = "http://localhost:8080/getTransfer/server/reserva"
URL_TEST = "https://facebook.github.io/react-native/movies.json"

# defining a params dict for the parameters to be sent to the API
#PARAMS = {'address':brasil}
PARAMS = ''

def requestData(endpoint):
	# sending get request and saving the response as response object 
	r = requests.get(url = endpoint, params = PARAMS)

	#XML parsers
	data = et.fromstring(r.content)

	for d in data:
		print d.text

	return 0


def runClient():
	screen = 0
	while(True):
		if (screen == 0):
			print ("# =========================== topTransfer.net =========================== #")
			print (" 1 - Check quotations")
			print (" 2 - Check bookings")
			print (" 0 - Exit")
		elif (screen == 1):
			print ("# ======================== quotations topTransfer ========================= #")
			#requestData(URL_COTACAO)
			print (" 1 - Book topTransfer")
			print (" 2 - Return")
		elif (screen == 2):
			print ("# ======================== Your bookings ========================= #")
			print ("2 - Return")

		# Get keyboard input
		op = raw_input("Option: ")

		if (screen == 0):
			if (op == '0'):
				break
			elif (op == '1'):
				screen = 1
			elif (op == '2'):
				screen = 2
		elif (screen == 1):
			if (op == '1'):
				print ("Lets reserva")
			elif (op == '2'):
				screen = 0
		elif (screen == 2):
			if (op == '2'):
				screen = 0

		os.system('CLS')


	return "Bye!"


if __name__ == "__main__":
	os.system('CLS')
	runClient()

	r = requests.get(URL_TEST)
	data = r.json()

	print (data['movies'][0]['title'])
