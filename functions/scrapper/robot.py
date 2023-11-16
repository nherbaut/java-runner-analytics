#!/usr/bin/env python3
import logging
import sys
import time
from pyvirtualdisplay import Display
from urllib.parse import urlparse, urlunparse
from selenium import webdriver
from selenium.webdriver.common.by import By
import random
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
            print(e,file=sys.stderr)
    except Exception as e:
        print(e,file=sys.stderr)
    
    
    
    

    # Wait for a random number of seconds between 3 and 10
    time_to_wait = random.randint(3, 10)
    time.sleep(time_to_wait)
    driver.get(url)
    driver.execute_script("window.scrollTo(0, 10000)")
    time.sleep(3)


    print(driver.page_source)
    exit()





