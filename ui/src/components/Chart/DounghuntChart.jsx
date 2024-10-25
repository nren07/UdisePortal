
import React, { useEffect, useRef } from 'react';
import Chart from 'chart.js/auto';

export default function DoughnutChart(props) {
  const chartRef = useRef(null);
  const chartInstance = useRef(null);
  
  const data = props.props|| {};
  
  console.log(props.props);
  console.log(props.props.accepted);
  
  const dataSet = [data.accepted || 0, data.other || 0]; 
  const labels = ['Accepted', 'Other', 'Budget Issue', 'Location Issue', 'Lost to Competitor', 'Site Visit', 'Not Connected'];
  useEffect(() => {
    if (chartInstance.current) {
      chartInstance.current.destroy();
    }
    const myChartRef = chartRef.current.getContext('2d');
    chartInstance.current = new Chart(myChartRef, {
      type: 'doughnut',
      data: {
        labels: labels,
        datasets: [
          {
            data: dataSet.concat(new Array(labels.length - dataSet.length).fill(0)), 
            backgroundColor: [
              'rgb(255, 99, 132)', 
              'rgb(255, 99, 132)',
              'rgb(255, 206, 86)',
              'rgb(255, 159, 64)', 
              'rgb(75, 192, 192)',              
              'rgb(54, 162, 235)',             
              'rgb(54, 162, 235)', 
            ],
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
      },
    });
    return () => {
      if (chartInstance.current) {
        chartInstance.current.destroy();
      }
    };
  }, [props.props]);

  return (
    <div style={{ height: 300, width: 300 }}>
      <canvas ref={chartRef} />
    </div>
  );
}
