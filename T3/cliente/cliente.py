#!/usr/bin/env python
# -*- coding: utf-8 -*-

import requests
import os
import json
import time

# api-endpoint
URL_COTACAO = "http://localhost:8080/getTransfer/server/app/cotacao?interesse="
URL_RESERVA = "http://localhost:8080/getTransfer/server/app/reserva?oferta="

# defining a params dict for the parameters to be sent to the API
#PARAMS = {'address':brasil}
PARAMS = ''

def runClient():
	screen = 0
	hardcode = 0
	getTransfer_msgs = ["Origin: ", "Destination: ", "Day: ", "Month: ", "Hour: ", "Vehicle type: ", "Number of passengers: ", "Price: "]
	getTransfer_args = []
	interesseCli = {}

	while(True):
		if (screen == 0):
			print ("# =========================== topTransfer.net =========================== #")
			# Menu
			print (" 1 - Check quotations")
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

			url = URL_COTACAO + interesseJson

			r = requests.get(url)

			cotacoes = r.json()
			lenCotacoes = len(cotacoes)
			#print (cotacoes)

			i = 0
			print (" \n # ======================================================================== # \n")
			for cotacao in cotacoes:
				print ("[{id}] Passengers: {pas}  | Price: {price}  | Vehicle: {veic}").format(id=i, pas=cotacao['passageiros'], price=cotacao['preco'], veic=cotacao['veiculo'])
				i = i + 1

			# Print options
			print ('\n')
			print ("Choose an offer by number or 'c' to cancel")

		elif (screen == 2):
			print (r.text)
			print ("Press any key to return")

		# Get keyboard input
		op = raw_input("Option: ")

		if (screen == 0):
			if (op == '0'):
				break
			elif (op == '1'):
				screen = 1
			elif (op == '3'):
				hardcode = 1
				interesseCli['itinerario'] = "Tokyo" + " - " + "Kyoto"
				interesseCli['dia'] = 23
				interesseCli['mes'] = 6
				interesseCli['hora'] = 12
				interesseCli['veiculo'] = "bmw"
				interesseCli['n_passageiros'] = 2
				interesseCli['preco'] = 25

		elif (screen == 1):
			if (op != 'c'):
				if (0 <= int(op) < lenCotacoes):
					ofertaCli = cotacoes[int(op)]
					ofertaJson = json.dumps(ofertaCli)
				
					url = URL_RESERVA + ofertaJson + '&interesse=' + interesseJson

					r = requests.get(url)

					screen = 2
				else:
					print "Not valid!"
					time.sleep(3)
					screen = 0
			else:
				screen = 0

			hardcode = 0
			interesseCli = {}
		elif (screen == 2):
				screen = 0			

		os.system('CLS')

	return "Bye!"


if __name__ == "__main__":
	os.system('CLS')
	runClient()

	#r = requests.get(URL_COTACAO)
	#data = r.json()

	#print data[0]
