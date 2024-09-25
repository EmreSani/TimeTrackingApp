import React, { useState, useEffect } from "react";
import "../../styles/signup.css"; 
import { useNavigate } from 'react-router-dom';

const DailyTimeEntries = () => {
  const [timeEntries, setTimeEntries] = useState([]);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const fetchDailyTimeEntries = async () => {
      const token = localStorage.getItem('token');
      if (!token) {
        setError("No token found, please log in again.");
        return;
      }

      try {
        const response = await fetch("http://localhost:8080/timeEntry/getAllDailyTimeEntriesByUser", {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
          },
        });

        if (response.ok) {
          const data = await response.json();
          console.log("Fetched Data:", data.object); 
          setTimeEntries(data.object || []); 
        } else {
          const errorData = await response.json();
          setError(`Error fetching time entries: ${errorData.message || 'Unknown error'}`);
        }
      } catch (error) {
        setError("Network error: " + error.message);
      }
    };

    fetchDailyTimeEntries();
  }, []);

  
  const calculateEndDateTime = (startDateTime, totalMinutesWorked) => {
    const start = new Date(startDateTime);
    const end = new Date(start.getTime() + totalMinutesWorked * 60000);
    return end;
  };

  return (
    <div className="daily-time-entries">
      <h2>Günlük Çalışma Saatleri</h2>
      {error && <p style={{ color: "red" }}>{error}</p>}
      <table>
        <thead>
          <tr>
            <th>Kurs Adı</th>
            <th>Başlangıç Zamanı</th>
            <th>Bitiş Zamanı</th>
            <th>Süre (dakika)</th>
          </tr>
        </thead>
        <tbody>
          {timeEntries.map((entry) => (
            <tr key={entry.timeEntryId}>
              <td>{entry.courseName || "Kurs Bilgisi Yok"}</td>
              <td>{entry.startDateTime ? new Date(entry.startDateTime).toLocaleString() : "Bilinmiyor"}</td>
              <td>
                {entry.startDateTime && entry.totalMinutesWorked
                  ? calculateEndDateTime(entry.startDateTime, entry.totalMinutesWorked).toLocaleString()
                  : "Bilinmiyor"}
              </td>
              <td>{entry.totalMinutesWorked || "Bilinmiyor"}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default DailyTimeEntries;
