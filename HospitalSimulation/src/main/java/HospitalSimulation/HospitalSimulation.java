package HospitalSimulation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import HospitalSimulation.Hospital.Medication;
import HospitalSimulation.Patient.HealthState;

/**
 * Class combines objects of Patient and Hospital. Here we interact
 * with user, process input data and print string representation of patients
 * health states to strout.
 * 
 * @author sabinaorazem
 * @since 23.10.2018
 * @version 1.1
 */
public class HospitalSimulation implements Serializable{
	
	private static final long serialVersionUID = 1L;
		
	private static Hospital hospital = new Hospital();
	
	private static HashMap<HealthState, Integer> mapPatMed = new HashMap<>();
	
	private static ArrayList<Medication> medsGivenToPatients = new ArrayList<>();
	private static ArrayList<Patient> healedPatients = new ArrayList<>();
	
	/**
	 * Method converts strin into objects Patient. Data is processed
	 * and data about healed patient is received. Method returns the health state of
	 * the patient after a visit to the hospital.
	 * 
	 * @param String patients
	 * @param String medications
	 * @return String mapToStr(mapPatMed)
	 */
	public static String generateOutput(String patients, String medications) {
		initialize();
		
		String[] listOfPatients = patients.toUpperCase().split(","); 
		String[] listOfMedications = medications.toUpperCase().split(",");
		
		if (listOfPatients.length == 0 || "".equals(listOfPatients[0])) { // ",H,H,T"
			return mapToStr(mapPatMed);
		}

		for (int j = 0; j < listOfMedications.length; j++) {
			try {
				if("".equals(listOfMedications[j])) 
					medsGivenToPatients.add(Medication.N);
				else
					medsGivenToPatients.add(Medication.valueOf(listOfMedications[j])); 
			} catch (IllegalArgumentException e) {
				return listOfMedications[j] + " is not valid medication." + Medication.displayValidMedications();
			}
		}

		for (int i = 0; i < listOfPatients.length; i++) {
			HealthState healthState = null;
			try {
				healthState = HealthState.valueOf(listOfPatients[i]);
			} catch (IllegalArgumentException e) {
				return listOfPatients[i] + " is not a valid health state. " + HealthState.displayValidHealthStates();
			}

			Patient patient = new Patient(healthState, medsGivenToPatients);
			Patient healedPatient = hospital.healPatient(patient);
			healedPatients.add(healedPatient);
			mapPatMed.computeIfPresent(healedPatient.getCurrentState(), (k,v) -> v+1);
		}
		return mapToStr(mapPatMed);
	}

	/**
	 * Initialize map with state of having no patients.
	 */
	private static void initialize() {
		mapPatMed.put(HealthState.F, 0);
		mapPatMed.put(HealthState.H, 0);
		mapPatMed.put(HealthState.D, 0);
		mapPatMed.put(HealthState.T, 0);
		mapPatMed.put(HealthState.X, 0);
	}
	
	/**
	 * Convert map representation of patients and health states to string that can
	 * be printed to strout.
	 * 
	 * @param map
	 * @return String
	 */
	private static String mapToStr(HashMap<HealthState, Integer> map) {
		String out = "";
		for (Map.Entry<HealthState, Integer> entry : map.entrySet()) {
			if (out != "") 
				out += ",";
			out += entry.getKey().toString() + ":";
			out += entry.getValue();
		}
		return out;
	}
	
	/**
	 * @return Hospital
	 */
	public static Hospital getHospital() {
		return hospital;
	}

	/**
	 * @param hospital
	 */
	public static void setHospital(Hospital hospital) {
		HospitalSimulation.hospital = hospital;
	}

	/**
	 * @return HashMap<HealthState, Integer>
	 */
	public static HashMap<HealthState, Integer> getMapPatMed() {
		return mapPatMed;
	}

	/**
	 * @param mapPatMed
	 */
	public static void setMapPatMed(HashMap<HealthState, Integer> mapPatMed) {
		HospitalSimulation.mapPatMed = mapPatMed;
	}

	/**
	 * @return ArrayList<Medication>
	 */
	public static ArrayList<Medication> getMedsGivenToPatients() {
		return medsGivenToPatients;
	}

	/**
	 * @param medsGivenToPatients
	 */
	public static void setMedsGivenToPatients(ArrayList<Medication> medsGivenToPatients) {
		HospitalSimulation.medsGivenToPatients = medsGivenToPatients;
	}

	/**
	 * @return ArrayList<Patient>
	 */
	public static ArrayList<Patient> getHealedPatients() {
		return healedPatients;
	}

	/**
	 * @param healedPatients
	 */
	public static void setHealedPatients(ArrayList<Patient> healedPatients) {
		HospitalSimulation.healedPatients = healedPatients;
	}
	
	/**
	 * Interaction with the user. Method prints to stdout.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {	
		if (args.length == 0) {
			System.out.println(generateOutput("", "")); 
		} else if (args.length == 1) {
			System.out.println(generateOutput(args[0], ""));
		} else if (args.length == 2) {
			System.out.println(generateOutput(args[0], args[1]));
		} else {
			System.out.println(
					"Too many arguments. Please enter two arguments; list of patients and list of medications.");
		}
		return;
	}
}