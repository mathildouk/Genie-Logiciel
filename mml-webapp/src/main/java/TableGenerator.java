import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.FileReader;

public class TableGenerator {
	//create an html file in order to read the csv file which contains results of all framework
	private String html_text = "<!DOCTYPE html>\n" + 
			"<html lang='en'> \n" + 
			"\n" + 
			"<head>\n" + 
			"<meta charset='utf-8'/>\n" + 
			"<link rel='stylesheet' type=\"text/css\" href='style.css' media='screen'/>\n" + "<style>\n" + 
			"tr:nth-child(even) {background-color: #f2f2f2;}\n" + 
			"</style>" +
			"</head>\n" + 
			"\n" + 
			"<body><center><div class='results-block'><table class='resultsTable' style='width:70%'>";
	public TableGenerator() {
        BufferedReader br = null;
        String csv_sep = ";";
        String line = "";

        
        try {
        	String csv_path = "scripts_upload/results/results.csv";
            br = new BufferedReader(new FileReader(csv_path));
            boolean first_line = true;
            String start_tag;
            String end_tag;
            while ((line = br.readLine()) != null) {
            	html_text += "<tr>";
            	if (first_line) {
        			start_tag = "<th class='custom-cell'>";
        			end_tag = "</th>";
            		first_line = false;
        		}
            	else {
        			start_tag = "<td class='custom-cell'>";
        			end_tag = "</td>";
            	}
            	for (String cell: line.split(csv_sep)) {
            		html_text += start_tag + cell + end_tag;
            	}
            	html_text += "</tr>";
            }
            html_text += "</table></div></center><body></html>";

        } catch (FileNotFoundException e) {
            html_text = "File not found  " ;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
	
	public String getHtml() {
		return this.html_text;
	}

}
