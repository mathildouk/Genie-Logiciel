import os
import glob
import pandas as pd
print(os.getcwd())
os.chdir(os.getcwd() +"/scripts_upload/results")
print(os.getcwd())
extension = 'csv'
all_filenames = [i for i in glob.glob('*.{}'.format(extension))]
#combine all files in the list
combined_csv = pd.concat([pd.read_csv(f) for f in all_filenames ])
#export to csv
combined_csv.to_csv( "results.csv", index=False, encoding='utf-8-sig')
