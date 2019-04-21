/**
 * Draw a pie chart using chartJs lib
 *
 * @param data to be applied to the chart
 * @param canvas where the chart will draw
 */
function drawPieChart(data, canvas) {

    var configuration = {
        type: 'polarArea',
        data: data,
        options: {
            responsive: true,
            legend: {
                position: 'left'
            },
            tooltips: {
                callbacks: {
                    label: function (tooltipItem, data) {
                        return " " + data.labels[tooltipItem.index];
                    }
                }
            }
        }
    };

    new Chart(document.getElementById(canvas).getContext('2d'), configuration);
}

/**
 * Draw a line chart using chartJS lib
 *
 * @param data chart data
 * @param canvas where the chart will be draw
 */
function drawLineChart(data, canvas) {

    var configuration = {
        type: 'line',
        data: data,
        options: {
            responsive: true,
            tooltips: {
                callbacks: {
                    label: function (tooltipItem, data) {
                        return " " + formatAsMonetaryValue(tooltipItem.value);
                    }
                }
            }
        }
    };

    new Chart(document.getElementById(canvas).getContext('2d'), configuration);
}

/**
 * Simple function to format the value as monetary value
 *
 * @param number to be formatted
 * @returns {string} the formatted number
 */
function formatAsMonetaryValue(number) {
    number += '';
    x = number.split('.');
    x1 = x[0];
    x2 = x.length > 1 ? ',' + x[1] : '';
    var rgx = /(\d+)(\d{3})/;
    while (rgx.test(x1)) {
        x1 = x1.replace(rgx, '$1' + '.' + '$2');
    }
    return "R$ " + x1 + x2;
}