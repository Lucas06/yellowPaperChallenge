import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    stages: [
        { duration: '1m', target: 100 }, // Increase to 100 concurrent users in 1 minute.
        { duration: '2m', target: 200 }, // Maintain 200 concurrent users for 2 minutes.
        { duration: '30s', target: 0 },  // Reduce to 0 users in 30 seconds.
    ],
};

export default function () {
    const petId = Math.floor(Math.random() * 1000000);  // Generates a random pet ID.
    const res = http.get(`http://localhost:8081/v3/pet/${petId}`);

    check(res, {
        'status is 200 or 404': (r) => r.status === 200 || r.status === 404,  // Verify whether the pet exists or not.
        'response time is acceptable': (r) => r.timings.duration < 200,  // Verify that the response time is less than 200 ms.
    });

    sleep(1);  // Wait 1 second between requests.
}

// k6 run src/test/performance/updatePetTest.js
