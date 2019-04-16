/**
 * Draw a pie chart using chartJs lib
 *
 * @param chartData to be applied to the chart
 * @param canvas where the chart will draw
 */
function drawPieChart(chartData, canvas) {

    var config = {
        type: 'pie',
        data: chartData,
        options: {
            responsive: true,
            legend: {
                position: 'left'
            },
            tooltips: {
                callbacks: {
                    label: function(tooltipItem, data) {
                        return " " + data.labels[tooltipItem.index];
                    }
                }
            }
        }
    };

    new Chart(document.getElementById(canvas).getContext('2d'), config);
}