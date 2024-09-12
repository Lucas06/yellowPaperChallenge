import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    stages: [
        { duration: '1m', target: 100 }, // Increases up to 100 concurrent users in 1 minute.
        { duration: '2m', target: 200 }, // Maintains 200 concurrent users for 2 minutes.
        { duration: '30s', target: 0 },  // Reduce to 0 users in 30 seconds.
    ],
};

export default function () {
    // Data to create a new pet
    const petData = {
        id: Math.floor(Math.random() * 1000000),  // Generates a random pet ID.
        name: `Pet-${Math.floor(Math.random() * 1000)}`,  // Generates a random pet name.
        status: 'available'
    };

    // Make a POST request to create a new pet
    const res = http.post('http://localhost:8081/api/v3/pet', JSON.stringify(petData), {
        headers: { 'Content-Type': 'application/json' },
    });

    check(res, {
        'status is 200': (r) => r.status === 200,  // Verify that the status is 200.
        'response time is acceptable': (r) => r.timings.duration < 200,  // Verify that the response time is less than 200 ms.
    });

    sleep(1);  // Wait 1 second between requests.
}

// k6 run src/test/performance/createPetTest.js