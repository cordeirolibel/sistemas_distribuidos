#!/usr/bin/env python
# -*- coding: utf-8 -*-

import requests
import os
import json

# api-endpoint
URL_COTACAO = "http://192.168.106.151:8080/getTransfer/server/app/cotacao"
URL_RESERVA = "http://192.168.106.151:8080/getTransfer/server/reserva"

# defining a params dict for the parameters to be sent to the API
#PARAMS = {'address':brasil}
PARAMS = ''

def runClient():
	screen = 0
	getTransfer_msgs = ["Origin: ", "Destination: ", "Day: ", "Month: ", "Hour: ", "Vehicle type: ", "Number of passengers: ", "Price: "]
	getTransfer_args = []
	interesseCli = {}

	while(True):
		if (screen == 0):
			print ("# =========================== topTransfer.net =========================== #")
			# Menu
			print (" 1 - Check quotations")
			print (" 2 - Check bookings")
			print (" 0 - Exit")
		elif (screen == 1):
			print ("# =========================== Cotations topTransfer.net =========================== #")

			if (not(hardcode)):
				for msg in getTransfer_msgs:
					getTransfer_args.append(raw_input(msg))

				interesseCli['itinerario'] = getTransfer_args[0] + " - " + getTransfer_args[1]
				interesseCli['dia'] = getTransfer_args[2]
				interesseCli['mes'] = getTransfer_args[3]
				interesseCli['hora'] = getTransfer_args[4]
				interesseCli['veiculo'] = getTransfer_args[5]
				interesseCli['n_passageiros'] = getTransfer_args[6]
				interesseCli['preco'] = getTransfer_args[7]
			else:
				print (interesseCli)

			interesseJson = json.dumps(interesseCli)

			url = URL_COTACAO + "/" + interesseJson

			r = requests.get(url)

			cotacoes = r.json()
			#print (cotacoes)

			print (" \n # ======================================================================== # \n")
			for cotacao in cotacoes:
				print ("[{id}] Passengers: {pas}  | Price: {price}  | Vehicle: {veic}").format(id=cotacao['id'], pas=cotacao['passageiros'], price=cotacao['preco'], veic=cotacao['veiculo'])

			# Print options
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
			elif (op == '3'):
				hardcode = 1
				interesseCli['itinerario'] = "Tokyo" + " - " + "Kyoto"
				interesseCli['dia'] = 23
				interesseCli['mes'] = 06
				interesseCli['hora'] = 12
				interesseCli['veiculo'] = "bmw"
				interesseCli['n_passageiros'] = 2
				interesseCli['preco'] = 25

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

	#r = requests.get(URL_COTACAO)
	#data = r.json()

	#print data[0]
