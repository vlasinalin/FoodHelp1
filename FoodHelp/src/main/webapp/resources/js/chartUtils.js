//<![CDATA[
function drawComboChart(nutrLabels, nutrTotal, foodRDA, chartTitle, divId) {
    var arr = [];
    var size = nutrLabels.length;
    for (var i=0; i<size; i++) {
        var obj = 
            [   nutrLabels[i].name, 
                parseFloat(Number(nutrTotal[i])), 
                parseFloat(Number(100)),
                createRecommTooltip(nutrLabels[i].name, foodRDA[i])
            ];
        arr.push(obj);
    }

    var dataTable = new google.visualization.DataTable();
    dataTable.addColumn('string', 'Nutrient');
    dataTable.addColumn('number', 'Quantity');
    dataTable.addColumn('number', 'Recommended');
    // A column for custom tooltip content
    dataTable.addColumn({'type': 'string', 'role': 'tooltip', 'p': {'html': true}});

    dataTable.addRows(arr);

    var formatter = new google.visualization.NumberFormat({
        fractionDigits: 2,
        suffix: '%'
    });

    formatter.format(dataTable, 1); // Apply formatter to second column.
    formatter.format(dataTable, 2); // Apply formatter to third column.

    var options_avg = {
      title: chartTitle,
      hAxis: {textStyle: {fontSize: 12}},
      vAxis: {maxValue: 200, minValue: 0, format: "#'%'", textStyle: {fontSize: 12}},
      seriesType: "bars",
      series: {1: {type: "line",color: 'lightgray'}},
      height: 300,
      chartArea:{left:60,top:50,width:"90%",height:"60%"},
      legend: {position: 'top', textStyle: {color: 'black', fontSize: 12}},
      tooltip: {isHtml: true}
    };

    var chart_avg = new google.visualization.ComboChart(document.getElementById(divId));
    chart_avg.draw(dataTable, options_avg);
}
//]]>

function obtainRDA(personId) {
    $.ajax(
    {
        type: "GET",
        url: "http://localhost:8080/FoodHelp/webresources/foodRecommendation/getRDA/" + personId,
        dataType: 'json',
        success: success_obtainRDA,
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("getRDA error: " + textStatus + " " + errorThrown + " " + XMLHttpRequest);
        }
    });
}

function obtainNutrLabels() {    
    $.ajax(
    {
        type: "GET",
        url: "http://localhost:8080/FoodHelp/webresources/foodRecommendation/getFoodNutrNames",
        dataType: 'json',
        success: success_obtainNutrLabels,
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("getFoodNutrNames error: " + textStatus + " " + errorThrown + " " + XMLHttpRequest);
        }
    });
}

function createRecommTooltip(label, value) {
    return '<div style="padding:8px; color:#323;">' +
        '<div style="font-weight:bold">' +
            label +
        '</div>' +
        '<div>' +
            'Recommended value: '+
            '<span style="font-weight:bold">' +
                value +
            '</span>' +
        '</div>' +
    '</div>';
}