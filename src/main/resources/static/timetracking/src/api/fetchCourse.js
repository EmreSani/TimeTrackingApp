export const fetchCourses = async (token, url) => {
  try {
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    });

    console.log('Response:', response);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();

    console.log('API Response:', data);

  
    if (data && Array.isArray(data.object)) {
      return data.object;
    } else {
      throw new Error('Unexpected data format');
    }
  } catch (error) {
    console.error('Error fetching courses:', error);
    throw error;
  }
};
