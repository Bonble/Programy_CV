import requests
from bs4 import BeautifulSoup
import time
import random
import numpy as np
import multiprocessing
from multiprocessing import Pool, freeze_support
from multiprocessing import Process, Queue
import sys
import pandas as pd
import pickle
import mysql.connector
import json
from datetime import date, datetime, timedelta
from dateutil.relativedelta import relativedelta


def kursy_historia():
    do = datetime.today().strftime('%Y-%m-%d')
    od = (datetime.now() - relativedelta(years=1)).strftime('%Y-%m-%d')

    date_format = '%Y-%m-%d'

    url = ["http://api.nbp.pl/api/exchangerates/rates/c/usd/" + od + "/" + do + "/?format=json",
           "http://api.nbp.pl/api/exchangerates/rates/c/chf/" + od + "/" + do + "/?format=json",
           "http://api.nbp.pl/api/exchangerates/rates/c/gbp/" + od + "/" + do + "/?format=json",
           "http://api.nbp.pl/api/exchangerates/rates/c/eur/" + od + "/" + do + "/?format=json"]

    waluty = ["usd", "chf", "gbp", "eur"]
    wyniki = []

    for u in url:
        response = requests.get(u)
        wynik = response.json()
        wyniki.append(wynik)
    mydb = mysql.connector.connect(
        host="localhost",
        user="root",
        password="",
        database="praca_dyplomowa",
        charset="utf8",
        use_unicode=True
    )
    cursor = mydb.cursor()

    for w in waluty:
        sql = "DELETE FROM kurs" + w
        cursor.execute(sql)
        mydb.commit()

    # print(wynik)
    # print(len(wynik["rates"]))
    # print(wynik["rates"][0]["effectiveDate"])
    i = 0
    for wynik in wyniki:
        ostatnia_data = datetime.strptime(wynik["rates"][0]["effectiveDate"], date_format) - timedelta(days=1)

        day2 = datetime.strptime(wynik["rates"][10]["effectiveDate"], date_format)

        # print("date1: ", ostatnia_data, " date2: ", day2)
        # print(delta)
        for x in wynik["rates"]:
            data_teraz = datetime.strptime(x["effectiveDate"], date_format)
            roznica = (data_teraz - ostatnia_data).days
            # print(roznica)
            if roznica == 1:
                sql = "INSERT INTO kurs" + waluty[i] + " (kupno, sprzedarz) VALUES (%s, %s)"
                val = (x["bid"], x["ask"])
                cursor.execute(sql, val)
                mydb.commit()
                # print(x)
            else:
                while roznica != 1:
                    sql = "INSERT INTO kurs" + waluty[i] + " (kupno, sprzedarz) VALUES (%s, %s)"
                    val = (x["bid"], x["ask"])
                    cursor.execute(sql, val)
                    mydb.commit()
                    roznica = roznica - 1
                sql = "INSERT INTO kurs" + waluty[i] + " (kupno, sprzedarz) VALUES (%s, %s)"
                val = (x["bid"], x["ask"])
                cursor.execute(sql, val)
                mydb.commit()
            ostatnia_data = data_teraz
        i = i + 1

if __name__ == '__main__':
    kursy_historia()