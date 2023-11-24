#!/usr/bin/env python3
import logging
import sys
import time
from pyvirtualdisplay import Display
from urllib.parse import urlparse, urlunparse
from selenium import webdriver
from selenium.webdriver.common.by import By
import random
import json
def remove_path_from_url(url):
    parsed_url = urlparse(url)
    # Reconstruct the URL without the path
    return urlunparse((parsed_url.scheme, parsed_url.netloc, '', '', '', ''))


logging.basicConfig(stream=sys.stderr, level=logging.INFO)

#Set a Display
display = Display(visible=0, size=(1280, 720))
display.start()
#logging.info ('Display Activated')


#Options for the ChromeDriver

opt = webdriver.ChromeOptions()
caps = webdriver.DesiredCapabilities.CHROME.copy()

opt.add_argument("--no-sandbox")
opt.add_argument("--disable-gpu")
opt.add_argument("--allow-running-insecure-content")
opt.add_argument("--ignore-ssl-errors=yes")
opt.add_argument("--window-size=1280,720")
opt.add_argument("--ignore-certificate-errors")
opt.add_argument("--disable-dev-shm-usage")
opt.set_capability( "goog:loggingPrefs", {"performance": "ALL"} );
def get_status(logs):
    for log in logs:
        if log['message']:
            d = json.loads(log['message'])
            try:
                content_type = 'text/html' in d['message']['params']['response']['headers']['content-type']
                response_received = d['message']['method'] == 'Network.responseReceived'
                if content_type and response_received:
                    return d['message']['params']['response']['status']
            except:
                pass


#Load the driver
driver = webdriver.Chrome(options=opt)

# Navigate to target website
for url in sys.stdin:
    try:
        driver.get(remove_path_from_url(url))
        time.sleep(4)
        first_link = driver.find_element(By.TAG_NAME, "a")
        driver.execute_script("window.scrollTo(0, 10000)")
        try:
            driver.get(remove_path_from_url(url))
            driver.execute_script("arguments[0].click();", first_link)
        except Exception as e:
           pass
    except Exception as e:
        pass
    
    
    
    

    # Wait for a random number of seconds between 3 and 10
    time_to_wait = random.randint(3, 10)
    time.sleep(time_to_wait)
    driver.get(url)
    logs = driver.get_log('performance')

    driver.execute_script("window.scrollTo(0, 10000)")
    time.sleep(3)

    print(driver.page_source)
    exit()





