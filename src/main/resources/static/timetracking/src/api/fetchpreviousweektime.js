import axios from 'axios';

//const API_URL_PREV_WEEK = 'http://localhost:8080/timeEntry/getAllPreviousWeekTimeEntriesByUser';


export const fetchPreviousWeekTimeEntries = async (token,url) => {
  try {
    const response = await axios.get(url, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    
    console.log('API Response for Previous Week Time Entries:', response.data);
    
    // `object` özelliği altında verileri döndürün
    if (response.data && Array.isArray(response.data.object)) {
      return response.data.object;
    } else {
      throw new Error('Unexpected data format');
    }
  } catch (error) {
    console.error('Error fetching previous week time entries:', error);
    throw error;
  }
};
