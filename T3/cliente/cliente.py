#!/usr/bin/env python
# -*- coding: utf-8 -*-

import requests
import os
import json
import time

# api-endpoint
URL_COTACAO = "http://localhost:8080/getTransfer2/webresources/app/cotacao?interesse="
URL_RESERVA = "http://localhost:8080/getTransfer2/webresources/app/reserva?oferta="


def runClient():
	screen = 0
	hardcode = 0
	getTransfer_msgs = ["Origin: ", "Destination: ", "Day: ", "Month: ", "Hour: ", "Vehicle type: ", "Number of passengers: ", "Price: "]
	getTransfer_args = []
	interesseCli = {}

	# loop principal
	while(True):
		# Tela inicial
		if (screen == 0): 
			print ("# =========================== topTransfer.net =========================== #")
			# Menu
			print (" 1 - Check quotations")
			print (" 0 - Exit")
		#Tela para constriur o interesse
		elif (screen == 1):
			print ("# =========================== Cotations topTransfer.net =========================== #")

			#le do usuario o interesse
			if (not(hardcode)):

				#le teclado
				for msg in getTransfer_msgs:
					getTransfer_args.append(raw_input(msg))

				#cria interesse 
				interesseCli['itinerario'] = getTransfer_args[0] + " - " + getTransfer_args[1]
				interesseCli['dia'] = getTransfer_args[2]
				interesseCli['mes'] = getTransfer_args[3]
				interesseCli['hora'] = getTransfer_args[4]
				interesseCli['veiculo'] = getTransfer_args[5]
				interesseCli['n_passageiros'] = getTransfer_args[6]
				interesseCli['preco'] = getTransfer_args[7]

			else: #atalho para digitar mais rapido 
				print (interesseCli)

			print (interesseCli)

			#prepara o get
			getTransfer_args = []
			interesseJson = json.dumps(interesseCli)

			url = URL_COTACAO + interesseJson

			#GET
			r = requests.get(url)

			#recebe a lista de cotacoes como resposta
			cotacoes = r.json()
			lenCotacoes = len(cotacoes)
			

			#print de todas as cotacoes
			i = 0
			print (" \n # ======================================================================== # \n")
			for cotacao in cotacoes:
				print ("[{id}] Passengers: {pas}  | Price: {price}  | Vehicle: {veic}").format(id=i, pas=cotacao['passageiros'], price=cotacao['preco'], veic=cotacao['veiculo'])
				i = i + 1

			# Print options
			print ('\n')
			print ("Choose an offer by number or 'c' to cancel")

		#tela de resultado
		elif (screen == 2):
			print (r.text)
			print ("Press any key to return")

		# Get keyboard input
		op = raw_input("Option: ")

		if (screen == 0):
			#exit
			if (op == '0'):
				break
			#leitura de interesse
			elif (op == '1'):
				screen = 1
			#atalho para digitar mais rapido 
			elif (op == '3'):
				#cria um interesse hardcode
				hardcode = 1
				interesseCli['itinerario'] = "Tokyo" + " - " + "Kyoto"
				interesseCli['dia'] = 23
				interesseCli['mes'] = 6
				interesseCli['hora'] = 12
				interesseCli['veiculo'] = "bmw"
				interesseCli['n_passageiros'] = 2
				interesseCli['preco'] = 25

		#tela de escolha de uma cotacao 
		elif (screen == 1):
			#se nao for cancelar
			if (op != 'c'):
				#verifica se eh uma opcao valida
				if (0 <= int(op) < lenCotacoes):
					#prepara dado para o get
					ofertaCli = cotacoes[int(op)]
					ofertaJson = json.dumps(ofertaCli)
				
					url = URL_RESERVA + ofertaJson + '&interesse=' + interesseJson

					#GET
					r = requests.get(url)

					#tela de resultado
					screen = 2
				else: #escolha nao valida, retorna para a tela inicial
					print "Not valid!"
					time.sleep(3)
					screen = 0
			#cancelar
			else:
				screen = 0

			#limpa variaveis
			hardcode = 0
			interesseCli = {}

		#tela de resultado sempre volta para a tela inicial
		elif (screen == 2):
			screen = 0			

		#limpa tela 
		os.system('cls' if os.name == 'nt' else 'clear')

	return "Bye!"



# main
if __name__ == "__main__":
	os.system('cls' if os.name == 'nt' else 'clear')
	runClient()


