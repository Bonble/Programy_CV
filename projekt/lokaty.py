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

url_list = ('http://www.xlokaty.pl/Oferty_lokat_banku_Alior_Bank-1.html',
            'http://www.xlokaty.pl/Oferty_lokat_banku_Credit_Agricole-12.html',
            'http://www.xlokaty.pl/Oferty_lokat_banku_Bank_Pocztowy-3.html',
            'http://www.xlokaty.pl/Oferty_lokat_banku_BGZ_BNP_Paribas-6.html',
            'http://www.xlokaty.pl/Oferty_lokat_banku_BOS-7.html',
            'http://www.xlokaty.pl/Oferty_lokat_banku_Citi_Handlowy-11.html',
            'http://www.xlokaty.pl/Oferty_lokat_banku_Inbank-41.html',
            'http://www.xlokaty.pl/Oferty_lokat_banku_ING_Bank_Slaski-20.html',
            'http://www.xlokaty.pl/Oferty_lokat_banku_mBank-24.html')

sys.setrecursionlimit(6000)


def scrape_url(url, q):
    results = requests.get(url)
    soup = BeautifulSoup(results.text, 'html.parser')

    bank = soup.find_all('div', class_='ranking')
    bank = bank[0].find_all('h1')
    bank = ([t.strip() for t in bank[0].get_text(separator=" - ").split(" - ")])

    d = []
    produkt = soup.find_all('td', class_='produkt')
    i = 0
    for p in produkt:
        d.append([t.strip() for t in produkt[i].get_text(separator="<br/>").split("<br/>")])
        i = i + 1
    produkt = d

    d = []
    proc_przed = []
    proc_po = []
    procent = soup.find_all('td', class_='procent')
    for p in procent:
        proc_przed.append(p.find_all('h2'))
        proc_po.append(p.find_all('h3'))

    anulowanie = soup.find_all('a', href="#mainrank")
    okres = soup.find_all('td', class_='okres')
    min = soup.find_all('td', class_='min')
    max = soup.find_all('td', class_='max')

    n = range(0, len(produkt))
    for x in n:
        produkt[x] = produkt[x][0]
        proc_przed[x] = proc_przed[x][0].get_text().split(">")[0].split("%")[0]
        proc_po[x] = proc_po[x][0].get_text().split("(")[1].split("%")[0]
        okres[x] = okres[x].get_text().split(">")[0].split()[0]
        min[x] = min[x].get_text().split(">")[0].split("z")[0]
        max[x] = max[x].get_text().split(">")[0].split("z")[0]
        anulowanie[x] = str(anulowanie[x]).split(" lokaty&lt;/b&gt;: ")[1].split(" - &lt;b&gt;informacje")[0]
        if (anulowanie[x] == "Całkowita utrata odsetek"):
            anulowanie[x] = "Utrata całości odsetek"
    # print(produkt)
    # print(proc_przed)
    """    
    try:
        print(produkt[0][0])
    except:
        print("niedziala-1")
    try:
        print(proc_przed[0][0].get_text().split(">")[0].split("%")[0])
    except:
        print("niedziala0")
    try:
        print(proc_po[0][0].get_text().split("(")[1].split("%")[0])
    except:
        print("niedziala1")
    try:
        print(okres[0].get_text().split(">")[0].split()[0])
    except:
        print("niedziala2")
    try:
        print(min[0].get_text().split(">")[0].split("z")[0])
    except:
        print("niedziala3")
    try:
        print(max[0].get_text().split(">")[0].split("z")[0])
    except:
        print("niedziala4")
    """
    # print(bank[1], produkt[0][0], proc_przed[0], proc_po[0], okres[0], min[0], max[0])
    # print("---")
    my_pickled_object = pickle.dumps(
        [bank[1], produkt, proc_przed, proc_po, okres, min, max, anulowanie])  # Pickling the object

    q.put(my_pickled_object)


def main():
    queue = multiprocessing.Queue()

    procs = []
    i = 0
    for u in url_list:
        procs.append(Process(target=scrape_url, args=(u, queue,)))
        i = i + 1

    for process in procs:
        process.start()

    result_set = []
    for p in procs:
        result_set.append(pickle.loads(queue.get()))

    for process in procs:
        process.join()

    # print(len(result_set))

    mydb = mysql.connector.connect(
        host="localhost",
        user="root",
        password="",
        database="praca_dyplomowa",
        charset="utf8",
        use_unicode=True
    )
    cursor = mydb.cursor()

    sql = "DELETE FROM lokaty"

    cursor.execute(sql)

    mydb.commit()

    # cursor.execute("SET NAMES utf8mb4;")  # or utf8 or any other charset you want to handle
    # cursor.execute("SET CHARACTER SET utf8mb4;")  # same as above
    # cursor.execute("SET character_set_connection=utf8mb4;")  # same as above
    for r in result_set:
        # print("----")
        # print(r)
        # print("----")

        n = range(0, len(r[1]))

        for x in n:
            sql = "INSERT INTO lokaty (bank, nazwa, procent, procent_po_opodatkowaniu, okres, minimum, maksimum, anulowanie) VALUES (%s, %s, %s, %s, %s, %s, %s, %s)"
            val = (
                r[0], r[1][x], r[2][x], r[3][x], r[4][x], r[5][x].replace(' ', ''), r[6][x].replace(' ', ''), r[7][x])
            cursor.execute(sql, val)
            mydb.commit()
            # print("bank: ", r[0])
            # print("nazwa: ", r[1][x])

        """
        col_dict = {'Produkt': r[1], 'Procent': r[2], 'Procent po opodatkowaniu': r[3], 'Okres': r[4],
                    'Minimum': r[5], 'Maksimum': r[6]}
        lok = pd.DataFrame(col_dict)
        #print("\nLokaty banku: ", r[0])
        #print(r[2])
        #print("----")
        #print(r[2][0])
        with pd.option_context('display.max_rows', None, 'display.max_columns',None):  # more options can be specified also
            print("xd")
            #print(lok,"\n")
        """



if __name__ == '__main__':
    main()