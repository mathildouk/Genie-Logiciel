import os
import glob
import pandas as pd
import numpy as np
os.chdir(os.getcwd()  +"/scripts_upload/results")
extension = 'csv'
all_filenames = [i for i in glob.glob('*.{}'.format(extension))]
files = [pd.read_csv(f,delimiter=';') for f in all_filenames ]
combined_csv = pd.concat(files)
combined_csv.to_csv( "results.csv", index=False, encoding='utf-8-sig', sep=";")
