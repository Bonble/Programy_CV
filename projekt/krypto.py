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

def krypto_historia():
    import requests
    #.replace(hour=1, minute=0, second=0)
    do = time.mktime(datetime.today().timetuple())
    od = time.mktime((datetime.now() - relativedelta(years=1)).timetuple())
    do = str(do).split('.')[0]
    od = str(od).split('.')[0]

    url = [
        "https://api.coingecko.com/api/v3/coins/bitcoin/market_chart/range?vs_currency=pln&from="+od+"&to="+do,
        "https://api.coingecko.com/api/v3/coins/ethereum/market_chart/range?vs_currency=pln&from="+od+"&to="+do,
        "https://api.coingecko.com/api/v3/coins/tether/market_chart/range?vs_currency=pln&from="+od+"&to="+do,
        "https://api.coingecko.com/api/v3/coins/usd-coin/market_chart/range?vs_currency=pln&from="+od+"&to="+do,
        "https://api.coingecko.com/api/v3/coins/euro-coin/market_chart/range?vs_currency=pln&from="+od+"&to="+do,
        "https://api.coingecko.com/api/v3/coins/tether-gold/market_chart/range?vs_currency=pln&from="+od+"&to="+do]
    wyniki = []
    waluty = ["bitcoin", "ethereum", "tether", "usd-coin", "euro-coin", "tether-gold"]
    headers = {
        "accept": "application/json",
        "x-cg-demo-api-key": "XXXXXXXXXXXXXXXXXXXXF"
    }

    for u in url:
        response = requests.get(u, headers=headers)
        wyniki.append(response.json())

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
        sql = "DELETE FROM `kurs_" + w+"`"
        cursor.execute(sql)
        mydb.commit()

    i = 0
    for wynik in wyniki:
        #print("\n", waluty[i])
        for x in wynik["prices"]:
            sql = "INSERT INTO `kurs_" + waluty[i] + "` (kurs) VALUES (%s)"
            val = x[1]
            cursor.execute(sql, (val,))
            mydb.commit()
            #print(x[1])
        #print(len(wynik["prices"]))
        i = i + 1


if __name__ == '__main__':
    krypto_historia()
