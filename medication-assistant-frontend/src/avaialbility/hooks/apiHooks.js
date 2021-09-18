import axios from 'axios';
import {useEffect, useState} from 'react';

export function useFindAllAvailabilitiesApi(page, pageSize) {
  const [response, setResponse] = useState({});
  useEffect(() => {
    const params = {page: page - 1, pageSize};
    axios
      .get('/api/availability', {params})
      .then(setResponse);
  }, [page, pageSize]);
  return {response};
}