package com.javaninjas.careerpathway.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.javaninjas.careerpathway.core.models.Job;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Job model.
 * Focuses on constructor, getters/setters, final field immutability, and toString.
 */
public class jobTest {

    // --- Test Data ---
    private static final int JOB_ID = 5001;
    private static final int COURSE_ID = 101;
    private static final String JOB_NAME = "Junior Software Developer";
    private static final String JOB_DESCRIPTION = "Entry-level development role in Java.";
    private static final int JOB_SALARY = 75000;

    // Helper method to create a standard Job object
    private Job createJob() {
        return new Job(JOB_ID, COURSE_ID, JOB_NAME, JOB_DESCRIPTION, JOB_SALARY);
    }

    // --- Constructor and Getters Tests ---

    @Test
    @DisplayName("Constructor and Getters: Should correctly initialize and retrieve all fields")
    void testConstructorAndGetters() {
        Job job = createJob();

        assertEquals(JOB_ID, job.getJobID(), "JobID must match the value passed to the constructor.");
        assertEquals(COURSE_ID, job.getCourseID(), "CourseID must match the value passed to the constructor.");
        assertEquals(JOB_NAME, job.getJobName(), "JobName must match the value passed to the constructor.");
        assertEquals(JOB_DESCRIPTION, job.getJobDescription(), "JobDescription must match the value passed to the constructor.");
        assertEquals(JOB_SALARY, job.getJobSalary(), "JobSalary must match the value passed to the constructor.");
    }
    
    @Test
    @DisplayName("get() method is an alias for getJobID and returns the correct value")
    void testGetAliasForJobID() {
        Job job = createJob();
        assertEquals(JOB_ID, job.get(), "The generic get() method should return the JobID.");
    }

    @Test
    @DisplayName("Final fields (jobID and courseID) should be immutable")
    void testFinalFieldsImmutability() {
        // Asserting the absence of setters for final fields
        assertThrows(NoSuchMethodException.class, () -> Job.class.getMethod("setJobID", int.class),
                "No setter for the final jobID field should exist.");
        assertThrows(NoSuchMethodException.class, () -> Job.class.getMethod("setCourseID", int.class),
                "No setter for the final courseID field should exist.");
    }
    
    // NOTE: JobSalary is NOT final, but no setter was provided in the original class.
    // The following test confirms the absence of the setter for jobSalary
    @Test
    @DisplayName("jobSalary is mutable but currently has no explicit setter")
    void testJobSalaryNoSetter() {
         assertThrows(NoSuchMethodException.class, () -> Job.class.getMethod("setJobSalary", int.class),
                 "No setter for jobSalary should exist based on the provided code.");
    }

    // --- Setters Tests ---

    @Test
    @DisplayName("setJobDescription: Should correctly update the job description")
    void testSetJobDescription() {
        Job job = createJob();
        String newDescription = "Senior role, leading a small team.";
        job.setJobDescription(newDescription);
        assertEquals(newDescription, job.getJobDescription(), "Job Description should be updated by setter.");
    }

    @Test
    @DisplayName("setJobName: Should correctly update the job name")
    void testSetJobName() {
        Job job = createJob();
        String newName = "Senior Java Engineer";
        job.setJobName(newName);
        assertEquals(newName, job.getJobName(), "Job Name should be updated by setter.");
    }

    @Test
    @DisplayName("Setters should handle null values gracefully")
    void testSetters_NullValues() {
        Job job = createJob();

        job.setJobDescription(null);
        job.setJobName(null);

        assertNull(job.getJobDescription(), "Job Description should be null.");
        assertNull(job.getJobName(), "Job Name should be null.");
    }

    // --- ToString Test ---

    @Test
    @DisplayName("toString: Should return only the jobName")
    void testToString() {
        Job job = createJob();
        assertEquals(JOB_NAME, job.toString(), "toString() should return the jobName.");

        // Test with a different name
        String shortName = "DevOps Engineer";
        job.setJobName(shortName);
        assertEquals(shortName, job.toString(), "toString() should reflect the updated jobName.");
    }
}