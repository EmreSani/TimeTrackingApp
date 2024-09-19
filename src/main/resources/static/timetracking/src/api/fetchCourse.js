import axios from 'axios';

//const API_URL = 'http://localhost:8080/course/getAllUsersCourses';

export const fetchCourses = async (token,url) => {
  try {
    const response = await axios.get(url, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    
    console.log('API Response:', response.data);
    
    // `object` özelliği altında kursları döndürün
    if (response.data && Array.isArray(response.data.object)) {
      return response.data.object;
    } else {
      throw new Error('Unexpected data format');
    }
  } catch (error) {
    console.error('Error fetching courses:', error);
    throw error;
  }
};

