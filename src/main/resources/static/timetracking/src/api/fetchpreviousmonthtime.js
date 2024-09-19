import axios from 'axios';

//const API_URL_PREV_MONTH = 'http://localhost:8080/timeEntry/getAllPreviousWeekTimeEntriesByUser';


export const fetchPreviousMonthTimeEntries = async (token,url) => {
  try {
    const response = await axios.get(url, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    
    console.log('API Response for Previous Month Time Entries:', response.data);
    
    // `object` özelliği altında verileri döndürün
    if (response.data && Array.isArray(response.data.object)) {
      return response.data.object;
    } else {
      throw new Error('Unexpected data format');
    }
  } catch (error) {
    console.error('Error fetching previous month time entries:', error);
    throw error;
  }
};
