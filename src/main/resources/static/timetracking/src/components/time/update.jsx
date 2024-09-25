import React, { useState, useEffect } from "react";
import "../../styles/signup.css"; 
import { useNavigate } from 'react-router-dom';

const UpdateTime = () => {
  const [timeEntries, setTimeEntries] = useState([]);
  const [selectedTimeEntryId, setSelectedTimeEntryId] = useState("");
  const [formData, setFormData] = useState({
    startDateTime: "",
    endDateTime: "",
    durationInMinutes: "",
    courseId: null,
  });
  const [error, setError] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const fetchTimeEntries = async () => {
      const token = localStorage.getItem('token');
      if (!token) {
        setError("No token found, please log in again.");
        return;
      }

      try {
        const response = await fetch("http://localhost:8080/timeEntry/allEntriesByUser", {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
          },
        });

        if (response.ok) {
          const data = await response.json();
          setTimeEntries(data.object || []);

          console.log("Fetched Data:", data.object); 
        } else {
          const errorData = await response.json();
          setError(`Error fetching time entries: ${errorData.message || 'Unknown error'}`);
        }
      } catch (error) {
        setError("Network error: " + error.message);
      }
    };

    fetchTimeEntries();
  }, []);

  useEffect(() => {
    const fetchTimeEntryData = async () => {
      if (!selectedTimeEntryId) return;

      const token = localStorage.getItem('token');
      if (!token) {
        setError("No token found, please log in again.");
        return;
      }

      try {
        const response = await fetch(`http://localhost:8080/timeEntry/${selectedTimeEntryId}`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
          },
        });

        if (response.ok) {
          const data = await response.json();
          const timeEntryData = data.object || data;

          setFormData({
            startDateTime: timeEntryData.startDateTime || "",
            endDateTime: timeEntryData.endDateTime || "",
            durationInMinutes: timeEntryData.durationInMinutes || "",
            courseId: timeEntryData.courseId || null,
          });
        } else {
          const errorData = await response.json();
          setError(`Error fetching time entry data: ${errorData.message || 'Unknown error'}`);
        }
      } catch (error) {
        setError("Network error: " + error.message);
      }
    };

    fetchTimeEntryData();
  }, [selectedTimeEntryId]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({ ...prevData, [name]: value }));
  };

  const handleSelectChange = (e) => {
    const selectedId = e.target.value;
    setSelectedTimeEntryId(selectedId);

    const selectedEntry = timeEntries.find(entry => entry.timeEntryId === parseInt(selectedId));

    if (selectedEntry) {
      setFormData({
        startDateTime: selectedEntry.startDateTime || "",
        endDateTime: selectedEntry.endDateTime || "",
        durationInMinutes: selectedEntry.totalMinutesWorked || "",
        courseId: selectedEntry.courseId || selectedEntry.course?.id || null,
      });
    } else {
      setFormData({ startDateTime: "", endDateTime: "", durationInMinutes: "" });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (!selectedTimeEntryId) {
      setError("Time entry ID is missing.");
      return;
    }

    if (!formData.courseId) {
      setError("Course ID is missing.");
      return;
    }

    const token = localStorage.getItem('token');
    if (!token) {
      setError("No token found, please log in again.");
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/timeEntry/updateTimeEntry/${selectedTimeEntryId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`,
        },
        body: JSON.stringify({
          ...formData,
          courseId: formData.courseId,
        }),
      });

      if (response.ok) {
        console.log("Time entry updated successfully!");
        setTimeout(() => {
          navigate("/home/daily");
        }, 2000);
      } else {
        const errorData = await response.json();
        setError(`Error updating time entry: ${errorData.message || 'Unknown error'}`);
      }
    } catch (error) {
      setError("Network error: " + error.message);
    }
  };

  return (
    <div className="signup-card">
      <h2>Update Time Entry</h2>
      {error && <p style={{ color: "red" }}>{error}</p>}

      <select onChange={handleSelectChange} value={selectedTimeEntryId}>
        <option value="">Select a Time Entry</option>
        {timeEntries.map((entry) => (
          <option key={entry.timeEntryId} value={entry.timeEntryId}>
            {entry.courseName ? `${entry.courseName} - ${new Date(entry.startDateTime).toLocaleString()}` : `No Course - ${new Date(entry.startDateTime).toLocaleString()}`}
          </option>
        ))}
      </select>

      <form onSubmit={handleSubmit}>
        <input
          type="datetime-local"
          name="startDateTime"
          placeholder="Start Date Time"
          value={formData.startDateTime.slice(0, 16)}
          onChange={handleInputChange}
          required
        />
        <input
          type="datetime-local"
          name="endDateTime"
          placeholder="End Date Time"
          value={formData.endDateTime.slice(0, 16)} 
          onChange={handleInputChange}
          required
        />
        
        <button className="button-reg" type="submit">Update Time Entry</button>
      </form>

      <button className="button-reg" onClick={() => navigate("/home/daily")}>
        Cancel
      </button>
    </div>
  );
};

export default UpdateTime;
