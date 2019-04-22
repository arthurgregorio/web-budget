/**
 * Draw a pie chart using chartJs lib
 *
 * @param data to be applied to the chart
 * @param canvas where the chart will draw
 */
function drawPieChart(data, canvas) {

    const configuration = {
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

    const formatter = new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL',
        minimumFractionDigits: 2
    });

    const configuration = {
        type: 'line',
        data: data,
        options: {
            responsive: true,
            tooltips: {
                callbacks: {
                    label: function (tooltipItem, data) {
                        return " " + formatter.format(tooltipItem.value);
                    }
                }
            }
        }
    };

    new Chart(document.getElementById(canvas).getContext('2d'), configuration);
}