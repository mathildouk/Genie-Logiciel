var NUMBER_OF_ALGORITHM = 0;

function showK() {
    document.getElementById("val_details").innerHTML = "K : <input type='number' id='kcrossval' name='kcrossval' min='1' max='100' value=1><br></br>";
};

function showSplit() {
    document.getElementById("val_details").innerHTML = "Test / Validation Split (%) : <br></br><input name='div_rate_choice' type='range' min='0' max='100' value='80' step='1'><label for='atpct' id='atpct-min'>0</label><label for='atpct' id='atpct-max'>100</label>";
}; 

/** Enables the .format method on strings similar to python.  */
if (!String.prototype.format) {
    String.prototype.format = function() {
      var args = arguments;
      return this.replace(/{(\d+)}/g, function(match, number) { 
        return typeof args[number] != 'undefined'
          ? args[number]
          : match
        ;
      });
    };
  }

function addAlgorithmBlock() {
    /**
     * This functions returns the html code corresponding to the form
     * for an algorithm.
     */

    var blocks = document.getElementById('algo_blocks');
    var new_div = blocks.appendChild(document.createElement('div'));
    new_div.id = 'div_algo' + NUMBER_OF_ALGORITHM.toString();
    new_div.className = "algo_block";
    // Algo.
    var html = "<select";
    html += " id=select_algo" + NUMBER_OF_ALGORITHM.toString();
    html += " name=select_algo" + NUMBER_OF_ALGORITHM.toString();
    html += " onChange=showDetailsAlgorithm(this.id)"  + ">";
    var options = ["Random Forest", "Decision Tree", "Logistic Regression", "XGBoost", "SVM"];
    for (i in options) {
        html += "<option value='{0}'>{0}</option>".format(options[i]);
    }
    html += "</select>"; 
    html += "<div id=algo_details" + NUMBER_OF_ALGORITHM.toString();
    html += " class=algo_details_block>";
    new_div.innerHTML = html;
    // Initialize the details block.
    showDetailsAlgorithm("select_algo" + NUMBER_OF_ALGORITHM.toString())
    NUMBER_OF_ALGORITHM += 1;
}

function removeAlgorithmBlock() {

  if (NUMBER_OF_ALGORITHM > 0) {
    NUMBER_OF_ALGORITHM -= 1;
  }
  else {
    NUMBER_OF_ALGORITHM = 0;
  }

  var blocks = document.getElementById('algo_blocks');
  var node_to_remove = document.getElementById(
    "div_algo" + NUMBER_OF_ALGORITHM.toString());
  blocks.removeChild(node_to_remove)
}

function showDetailsAlgorithm(select_id) {
  select_node = document.getElementById(select_id);
  details_node = select_node.parentNode.childNodes[1]
  no_parameters = "<p style='color:#A8A8A8'><i>No parameters.</i></p>"
  switch (select_node.value) {
    case "Random Forest":
      showDefault(details_node);
      break;
    case "Decision Tree":
      showDetailsDecisionTree(details_node)
      break;
    case "Logistic Regression":
      showDefault(details_node);
      break;
    case "XGBoost":
      showDefault(details_node);
      break;
    case "SVM":
      showDetailsSVM(details_node);
      break;
    default:
      console.log('Default');
  }
}

function showDefault(details_node) {
  // Framework.
  algo_nb = details_node.id.match(/(\d+)/)[0];
  html = "Framework : "
  html += "<select";
  html += " id=select_fw" + algo_nb.toString();
  html += " name=select_fw" + algo_nb.toString() + ">"; 
  var options = ["scikit-learn", "R"];
  for (i in options) {
      html += "<option value='{0}'>{0}</option>".format(options[i]);
  };
  html += "</select><br></br>"; 
  details_node.innerHTML = html;
}

function showDetailsDecisionTree(details_node) {
  algo_nb = details_node.id.match(/(\d+)/)[0];
  // Framework.
  html = "Framework : "
  html += "<select";
  html += " id=select_fw" + algo_nb.toString();
  html += " name=select_fw" + algo_nb.toString() + ">"; 
  var options = ["scikit-learn", "R"];
  for (i in options) {
      html += "<option value='{0}'>{0}</option>".format(options[i]);
  };
  html += "</select><br></br>"; 
  // Max Depth.
  html += "Max Depth :<br></br>"
  html += "<input id='dt_maxdepth" + algo_nb.toString() + "'";
  html += " name='dt_maxdepth" + algo_nb.toString() + "'";
  html += " type='range' min='1' max='30' value='30' step='1'><label for='atpct' id='atpct-min'>30</label><label for='atpct' id='atpct-max'></label></div>";
  details_node.innerHTML = html;
}

function showDetailsSVM(details_node) {
  algo_nb = details_node.id.match(/(\d+)/)[0];
  // Framework.
  html = "Framework : "
  html += "<select";
  html += " id=select_fw" + algo_nb.toString();
  html += " name=select_fw" + algo_nb.toString() + ">"; 
  var options = ["scikit-learn", "R"];
  for (i in options) {
      html += "<option value='{0}'>{0}</option>".format(options[i]);
  };
  html += "</select><br></br>";
  // Choice Gamma.
  html += "Gamma : <input type='number' name='gamma" + algo_nb.toString() + "'";
  html += " min='1' max='100' value=1 step='0.01'><br></br>";
  // Choice C.
  html += "C : <input type='number' name='c_choice" + algo_nb.toString() + "'";
  html += " min='1' max='100' value=1 step='0.01'><br></br>";
  // Choice Kernel.
  html += "Kernel : <select ";
  html += "name='select_kernel" + algo_nb.toString() + "'" + ">";
  var options = ["linear", "polynomial", "radial"];
  for (i in options) {
      html += "<option value='{0}'>{0}</option>".format(options[i]);
  }
  html += "</select>";
  // Choice Classfication.
  html += "<br></br>Classification : <select";
  html += " name='select_classification" + algo_nb.toString() + "'" + ">";
  var options = ["C-classification", "nu-classification", "one-classification"];
  for (i in options) {
      html += "<option value='{0}'>{0}</option>".format(options[i]);
  } 
  html += "</select>";
  details_node.innerHTML = html;
}

function showFormElements() {
  to_show = document.getElementById("main_form").elements;
  console.log(to_show)
}
