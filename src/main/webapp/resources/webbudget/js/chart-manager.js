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
            responsive: true
        }
    };

    new Chart(document.getElementById(canvas).getContext('2d'), configuration);
}