package Classification_Point;

/*
Source code file name: "pg0018_vb0018DT.java"
Date: 03/29/2017
References: 1. CS641 Class Notes, Dr. Ramazan Aygun
			2. 
			3. 
			4. 
			5. http://www.hub4tech.com/			
			6. http://stackoverflow.com/
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.regex.Pattern;

import Array_list.Array_list;
import Tree.TreeNode;
import java.util.Iterator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.math.*;

public class pg0018_vb0018DT {

	static Array_list[] class_data = new Array_list[2000];
	static TreeNode<Array_list> root;
	static ArrayList<Integer> Ignore_List = new ArrayList<Integer>();

	// Parent_attr_ID holds the value of the attribute number from the arff
	// (dataset)//
	// Parent_index holds the index of the parent node in the tree //
	public static void SubTrees_Formation(ArrayList<ArrayList<String>> data_Attributes, int Parent_attr_ID,
			TreeNode<Array_list> Parent_Data, int index) {
		//System.out.println("index  " + index + " Parent ID " + Parent_attr_ID);
		//System.out.println("Parent Attribute  " + Parent_Data.data.get_max_Attribute());
		// Following the data to the temporary Array-list
		//System.out.println(" Array_size" + Parent_Data.data.Data.size());
		if (Parent_attr_ID == Integer.MAX_VALUE)
			return;

		class_data[index] = new Array_list();
		class_data[index + 1] = new Array_list();
		class_data[index + 2] = new Array_list();

		ArrayList<ArrayList<String>> temporary_zero = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> temporary_one = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> temporary_two = new ArrayList<ArrayList<String>>();

		for (int i = 0; i < Parent_Data.data.Data.size(); i++) {
			if (Parent_Data.data.Data.get(i).get(Parent_attr_ID).equals("0")) {
				temporary_zero.add(Parent_Data.data.Data.get(i));
			} else if (Parent_Data.data.Data.get(i).get(Parent_attr_ID).equals("1")) {
				temporary_one.add(Parent_Data.data.Data.get(i));
			} else if (Parent_Data.data.Data.get(i).get(Parent_attr_ID).equals("2")) {
				temporary_two.add(Parent_Data.data.Data.get(i));
			}
		}

		Classification(data_Attributes, temporary_zero, index, 0);
		Classification(data_Attributes, temporary_one, index + 1, 1);
		Classification(data_Attributes, temporary_two, index + 2, 2);

		class_data[index].set_Array_list(temporary_zero);
		class_data[index + 1].set_Array_list(temporary_one);
		class_data[index + 2].set_Array_list(temporary_two);

		class_data[index].set_Parent_value(0);
		class_data[index + 1].set_Parent_value(1);
		class_data[index + 2].set_Parent_value(2);

	}

	public static void Classification(ArrayList<ArrayList<String>> data_Attributes,
			ArrayList<ArrayList<String>> data_Values, int index, int value) {

		int max_ID = Integer.MAX_VALUE;
		double max = Double.MIN_VALUE;

		double Entropy_data_zero = Calculate_data_Entropy(data_Values);
		double Entropy_Attribute = 0;
		double IG_Attribute[] = new double[9];
		//System.out.println("Array_List" + data_Values);
		//System.out.println("Array_size" + data_Values.size());
		for (int ID = 0; ID <= 8; ID++) {
			// System.out.println("\n" + ID + "\n");
			// System.out.println("\n" + Ignore_List + "\n");
			// if (Ignore_List.contains(ID)) {
			// Entropy_Attribute = 1;
			// } else {
			Entropy_Attribute = Calculate_data_Attribute(data_Values, ID);
			// }

			IG_Attribute[ID] = Entropy_data_zero - Entropy_Attribute;
			/*System.out.print("IG_Attribute[" + ID + "] " + IG_Attribute[ID] + " Entropy_data " + Entropy_data_zero
					+ " Entropy_Attribute " + Entropy_Attribute + "\n");*/
		}

		for (int i = 0; i < IG_Attribute.length; i++) {
			if (max < IG_Attribute[i]) {
				max_ID = i;
				max = IG_Attribute[i];
			}
		}

		//System.out.println("max_ID  " + max_ID + "  Max_IG_value  " + max + " value " + value);

		if (data_Attributes.size() >= max_ID)
			class_data[index].set_max_Attribute(data_Attributes.get(max_ID + 1).get(1));

		if (max_ID != Integer.MAX_VALUE)
			class_data[index].set_max_ID(max_ID);
/*
		if (data_Attributes.size() >= max_ID)
			System.out.println(" Selected Attribute  " + data_Attributes.get(max_ID + 1).get(1));*/

		class_data[index].set_max_IG(max);
		class_data[index].set_Class_value(value);

	}

	public static double Calculate_data_Entropy(ArrayList<ArrayList<String>> data_Values) {

		// System.out.println(data_Values.size());
		double twos_ct = 0.0;
		double fours_ct = 0.0;

		if (data_Values.size() < 1)
			return 0;

		for (int j = 0; j < data_Values.size(); j++) {

			if (data_Values.get(j).get(9).equals("2")) {
				// System.out.print("SUCCESS\n");
				twos_ct++;
			} else if (data_Values.get(j).get(9).equals("4")) {
				// System.out.print("SUCCESS\n");
				fours_ct++;
			}
		}

		double twos_prob = twos_ct / data_Values.size();
		double four_prob = fours_ct / data_Values.size();

		if (twos_prob == 0)
			twos_prob = 1;
		if (four_prob == 0)
			four_prob = 1;

		// System.out.println("twos_ct " + twos_ct); System.out.println(
		// "fours_ct " + fours_ct); System.out.println("twos_prob " +
		// twos_prob); System.out.println("four_prob " + four_prob);

		// System.out.println("\n" + "twos_prob " + twos_prob + "\n");
		// System.out.println("\n" + "fours_prob " + four_prob + "\n");

		double Entropy_overall = -1
				* (twos_prob * Math.log(twos_prob) / Math.log(2) + four_prob * Math.log(four_prob) / Math.log(2));
		// System.out.println("Entropy_overall " + Entropy_overall);

		return Entropy_overall;

	}

	public static double Calculate_data_Attribute(ArrayList<ArrayList<String>> data_Values, int Attr_ID) {
		/*
		 * StringBuilder sb = new StringBuilder(); for (ArrayList<String>
		 * arrayList: data_Values) { for (String s: arrayList) { sb.append(s);
		 * sb.append('\t'); } sb.append('\n'); } String yourInfo =
		 * sb.toString(); System.out.print(yourInfo);
		 * 
		 * 
		 */

		if (data_Values.size() < 1)
			return 0;

		double zero_ct = 0;
		double one_ct = 0;
		double two_ct = 0;

		ArrayList<ArrayList<String>> zeros_list = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> ones_list = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> twos_list = new ArrayList<ArrayList<String>>();

		for (int j = 0; j < data_Values.size(); j++) {

			if (data_Values.get(j).get(Attr_ID).equals("0")) {
				zeros_list.add(new ArrayList<String>(data_Values.get(j)));
				zero_ct++;

			} else if (data_Values.get(j).get(Attr_ID).equals("1")) {
				ones_list.add(new ArrayList<String>(data_Values.get(j)));
				one_ct++;
			} else if (data_Values.get(j).get(Attr_ID).equals("2")) {
				twos_list.add(new ArrayList<String>(data_Values.get(j)));
				two_ct++;
			}

		}

		double Entropy_zero = Calculate_data_Entropy(zeros_list);
		double Entropy_one = Calculate_data_Entropy(ones_list);
		double Entropy_two = Calculate_data_Entropy(twos_list);

		double zero_prob = zero_ct / (zero_ct + one_ct + two_ct);
		double one_prob = one_ct / (zero_ct + one_ct + two_ct);
		double two_prob = two_ct / (zero_ct + one_ct + two_ct);

		if (zero_prob == 0.0)
			zero_prob = 1.0;
		if (one_prob == 0.0)
			one_prob = 1.0;
		if (two_prob == 0.0)
			two_prob = 1.0;

/*		System.out.println(
				"Entropy_zero  " + Entropy_zero + "Entropy_one  " + Entropy_one + "Entropy_two  " + Entropy_two);*/

		double Entropy_Attribute = zero_prob * Entropy_zero + one_prob * Entropy_one + two_prob * Entropy_two;
		/*
		 * System.out.print(Entropy_Attribute); System.out.print("\n");
		 * System.out.print("\n");
		 */
		return Entropy_Attribute;

	}

	public static void main(String args[]) {

		// The name of the file to open.
		String fileName = "bcwdisc.arff";
		ArrayList<ArrayList<String>> data_Attributes = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> data_Values = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> Complete_data_Values = new ArrayList<ArrayList<String>>();

		// This will reference one line at a time
		String line = null;

		// Location of file
		String location_File = null;
		
		double percentage = 75;
		double percentage_data_size = 0;

		// Argument validation to check the availability of input file name and percentage//
		if (args.length < 2) {
			location_File = fileName;
			System.out.println(
					"\n Provide input file name in the format of pg0018_vb0018DT -i <InputFile> -c <classattribute> -T M");
			// return;
		}

		// Scraping file path name from arguments//
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-i")) {
				location_File = args[i + 1];
			}
		}
		
		// Scraping percentage from arguments//
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-T")) {
				percentage = Double.parseDouble(args[i + 1]);
			}
		}

		// Scraping input file name from the file path//
		String[] location_File_Split = location_File.split(Pattern.quote("/"));
		String inputFileName = location_File_Split[location_File_Split.length - 1];

		// Creating output files
		File Tree_File = new File("pg0018_vb0018DTTrain" + percentage + inputFileName); // To print the tree //
		File Value_File = new File("pg0018_vb0018DTApply" + percentage + inputFileName); // To print with the new attribute//
		File Confusion_Matrix_File = new File("pg0018_vb0018DTAccuracy" + percentage + inputFileName); // To print the confusion matrix //

		try {

			FileReader fileReader = new FileReader(fileName);

			// Normal write//
			FileWriter absolute_write_output1 = new FileWriter(Tree_File.getAbsoluteFile());
			// Buffered writing for efficiency to prevent conversion of
			// characters into bytes.//
			BufferedWriter buffer_write_output1 = new BufferedWriter(absolute_write_output1);

			FileWriter absolute_write_output2 = new FileWriter(Value_File.getAbsoluteFile());
			BufferedWriter buffer_write_output2 = new BufferedWriter(absolute_write_output2);

			FileWriter absolute_write_output3 = new FileWriter(Confusion_Matrix_File.getAbsoluteFile());
			BufferedWriter buffer_write_output3 = new BufferedWriter(absolute_write_output3);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedRead = new BufferedReader(fileReader);
			boolean readline = false;
			while ((line = bufferedRead.readLine()) != null) {

				if (!readline) {
					buffer_write_output2.write(line);
					buffer_write_output2.write("\n");
					if (line.contains("class")) {
						buffer_write_output2.write("@attribute dt_class real");
						buffer_write_output2.write("\n");
					}
				}

				if (line.equals("@data")) {
					readline = true;
					continue;
				}

				if ((line.length() > 2)) {
					if (!readline) {
						String[] lineSplit = line.trim().split("\\s+");
						// System.out.println(Arrays.toString(lineSplit));
						// System.out.println(Arrays.toString(lineSplit1));
						data_Attributes.add(new ArrayList<String>(Arrays.asList(lineSplit)));
					} else if (readline) {
						// if (rl_ct < m) {
						String[] lineSplit = line.trim().split("\\s+");
						// System.out.println(Arrays.toString(lineSplit));
						Complete_data_Values.add(new ArrayList<String>(Arrays.asList(lineSplit)));

					}

				}

			}

			percentage_data_size = Math.ceil(Complete_data_Values.size() * percentage / 100);

			for (int i = 0; i < percentage_data_size; i++)
				data_Values.add(Complete_data_Values.get(i));

			// Always close files.
			bufferedRead.close();



			double Entropy_Attribute[] = new double[9];
			double IG_Attribute[] = new double[9];
			double Entropy_data = Calculate_data_Entropy(data_Values);


			// System.out.println("Root");

			for (int ID = 0; ID <= 8; ID++) {
				Entropy_Attribute[ID] = Calculate_data_Attribute(data_Values, ID);
				IG_Attribute[ID] = Entropy_data - Entropy_Attribute[ID];
				// System.out.print("IG_Attribute[" + ID + "] " +
				// IG_Attribute[ID] + "\n");
			}

			/* Finding the max Information Gain value */
			Integer max_ID = 10;
			double max = Double.MIN_VALUE;
			for (int i = 0; i < IG_Attribute.length; i++) {
				if (max < IG_Attribute[i]) {
					max_ID = i;
					max = IG_Attribute[i];
				}
			}
			//System.out.println("max_ID  " + max_ID + "  Max_IG_" + "value  " + max);

			/* The Root */
			// class_data[0] = new Array_list(max_ID, max, -1, Child_array, 0);
			class_data[0] = new Array_list();
			class_data[0].set_Array_list(data_Values);
			class_data[0].set_max_ID(max_ID);
			class_data[0].set_max_IG(max);
			class_data[0].set_Parent_value(0);
			// class_data[0].print_Array_list();

			if (data_Attributes.size() >= max_ID)
				class_data[0].set_max_Attribute(data_Attributes.get(max_ID + 1).get(1));

/*			if (data_Attributes.size() >= max_ID)
				System.out.println(data_Attributes.get(max_ID + 1).get(1));*/

			// For indexing the nodes //

			int i = 1;
			root = new TreeNode<Array_list>(class_data[0]);
			TreeNode<Array_list> temp_Parent = root;

			// System.out.println(temp_Parent.data.get_max_IG());
			Integer List_counter = 1;
			/// Integer dummy_counter = 1;

			while (temp_Parent != null) {
				if ((temp_Parent.data.get_max_IG() == Double.MIN_VALUE) || (temp_Parent.data.Data.size() < 2)) {

					temp_Parent = root.next(List_counter);
					//System.out.println("temp_Parent " + temp_Parent);
					//System.out.println("Linked List counter " + List_counter);
					//root.print_linked_list();
					List_counter++;
					continue;
				} else {

					// if(temp_Parent.data.Data.size() > 1){
					//System.out.println(" parent node " + temp_Parent);
					// System.out.println("TEMP CHECK" + temp_Parent.data.Data);

					SubTrees_Formation(data_Attributes, temp_Parent.data.get_max_ID(), temp_Parent, i);

					if (class_data[i].Data.size() != 0)
						temp_Parent.addChild(class_data[i]);

					if (class_data[i + 1].Data.size() != 0)
						temp_Parent.addChild(class_data[i + 1]);

					if (class_data[i + 2].Data.size() != 0)
						temp_Parent.addChild(class_data[i + 2]);

					// I0terator<TreeNode<Array_list>> Next =
					// root.elementsIndex.iterator();
					i += 3;
					temp_Parent = root.next(List_counter);
					//System.out.println("temp_Parent " + temp_Parent);
					//System.out.println("Linked List " + List_counter);
					//root.print_linked_list();
					List_counter++;

				}

			}

			System.out.println("\n****** Complete Tree Generation ********* \n");
			buffer_write_output1.write("\n********************** Complete Tree Generation ********************** \n");

			TreeNode<Array_list> treeRoot = pg0018_vb0018DT.getSet1();
			for (TreeNode<Array_list> node : treeRoot) {
				String indent = createIndent(node.getLevel());
				if (node.parent != null) {
					System.out.print(
							indent + node.parent.data.get_max_Attribute() + " = " + node.data.get_Parent_value());
					buffer_write_output1.write(indent + node.parent.data.get_max_Attribute() + " = " + node.data.get_Parent_value());
					if (node.children.size() == 0) {
						if (node.data.get_max_Attribute().equals("default")){
							System.out.print(" " + node.data.Data.get(0).get(9));
							buffer_write_output1.write(" " + node.data.Data.get(0).get(9));
						}
						else {
							System.out.println();
							buffer_write_output1.write("\n");
							System.out.print(indent + " | " + node.data.get_max_Attribute() + " = "
									+ node.data.Data.get(2).get(node.data.get_max_ID()) + " "
									+ node.data.Data.get(2).get(9));
							buffer_write_output1.write(indent + " | " + node.data.get_max_Attribute() + " = "
									+ node.data.Data.get(2).get(node.data.get_max_ID()) + " "
									+ node.data.Data.get(2).get(9));
						}
					}
					System.out.println();
					buffer_write_output1.write("\n");
				}
			}

			/*
			 * // ***************** Code to traverse the tree *****************
			 */

			Integer search_counter = 0;
			Integer two_two_count = 0;
			Integer two_four_count = 0;
			Integer four_two_count = 0;
			Integer four_four_count = 0;
			Integer twos_majority_count = 0;
			Integer fours_majority_count = 0;

			while (search_counter < Complete_data_Values.size()) {
				TreeNode<Array_list> temp = root;
				TreeNode<Array_list> compare_temp = root;
				while (temp.search(0) != null) {
					compare_temp = temp;
					for (int i1 = 0; i1 < 3; i1++) {
						if (temp.search(i1) != null) {
							Integer Compare1 = temp.search(i1).data.get_Class_value();
							Integer Compare2 = Integer
									.parseInt(Complete_data_Values.get(search_counter).get(temp.data.get_max_ID()));
							if (Compare1 == Compare2) {
								// System.out.println("Reached");
								temp = temp.search(i1);
							}
						}
					}

					if (compare_temp == temp) {
						break;
					}

				}

				if (compare_temp == temp) {

					for (int i11 = 0; i11 < temp.data.Data.size(); i11++) {
						if (temp.data.Data.get(i11).get(9).equals("2")) {
							twos_majority_count++;
						} else if (temp.data.Data.get(i11).get(9).equals("4")) {
							fours_majority_count++;
						}

					}

					if (twos_majority_count > fours_majority_count)
						Complete_data_Values.get(search_counter).add("2");
					else
						Complete_data_Values.get(search_counter).add("4");

				}
				// System.out.println("SEARCH COUNTER" + search_counter +
				// "\n\n");
				else {

					Integer two_count = 0;
					Integer four_count = 0;
					for (int i12 = 0; i12 < temp.data.Data.size(); i12++) {
						if (temp.data.Data.get(i12).get(9).equals("2"))
							two_count++;
						else if (temp.data.Data.get(i12).get(9).equals("4"))
							four_count++;
					}
					if (two_count < four_count)
						Complete_data_Values.get(search_counter).add("4");
					else
						Complete_data_Values.get(search_counter).add("2");

				}

				//
				//System.out.println(Complete_data_Values.get(search_counter).get(10));
				if (Complete_data_Values.get(search_counter).get(9).equals("2")) {
					if (Complete_data_Values.get(search_counter).get(10).equals("2"))
						two_two_count++;
					else if (Complete_data_Values.get(search_counter).get(10).equals("4"))
						two_four_count++;
				}

				else if (Complete_data_Values.get(search_counter).get(9).equals("4")) {
					if (Complete_data_Values.get(search_counter).get(10).equals("4"))
						four_four_count++;
					if (Complete_data_Values.get(search_counter).get(10).equals("2"))
						four_two_count++;
				}

				//System.out.println(Complete_data_Values.get(search_counter));

				search_counter++;
			}
			
			// Writing the Final ArrayList of ArrayList to the output file //

			StringBuilder sb = new StringBuilder();
			for (ArrayList<String> arrayList : Complete_data_Values) {
				for (String s : arrayList) {
					sb.append(s);
					sb.append('\t');
				}
				sb.append('\n');
			}
			String Data_Value = sb.toString();
			System.out.println(Data_Value);
			buffer_write_output2.write(Data_Value);

			double Accuracy = (double) (two_two_count + four_four_count)
					/ (two_two_count + four_four_count + two_four_count + four_two_count);
			int Total_Count = two_two_count + four_four_count + two_four_count + four_two_count;
			
			System.out.println("\n\n***************CONFUSION MATRIX***************\n");
					
			System.out.print("Total = " + Total_Count + "\t");
			System.out.print("Predicted 2's\t");
			System.out.print("Predicted 4's\n");
			System.out.print("Actual 2's\t");
			System.out.print(two_two_count  + "\t\t");
			System.out.print(two_four_count + "\n");
			System.out.print("Predicted 4's\t");
			System.out.print(four_two_count  + "\t\t");
			System.out.print(four_four_count);
			
			System.out.println("\n\n***************ACCURACY***************\n\n" + "Accuracy = " + 100 * Accuracy + "%");
			
			buffer_write_output3.write("\n\n***************CONFUSION MATRIX***************\n");
			
			buffer_write_output3.write("Total = " + Total_Count + "\t\t\t");
			buffer_write_output3.write("Predicted 2's\t\t\t");
			buffer_write_output3.write("Predicted 4's\n");
			buffer_write_output3.write("Actual 2's\t\t\t\t");
			buffer_write_output3.write(two_two_count  + "\t\t\t\t\t\t");
			buffer_write_output3.write(two_four_count + "\n");
			buffer_write_output3.write("Predicted 4's\t\t\t");
			buffer_write_output3.write(four_two_count  + "\t\t\t\t\t\t");
			buffer_write_output3.write(four_four_count+ "\t");
			
			buffer_write_output3.write("\n\n***************ACCURACY***************\n\n" + "Accuracy = " + 100 * Accuracy + "%");
			

			buffer_write_output1.close();
			absolute_write_output1.close();
			buffer_write_output2.close();
			absolute_write_output2.close();
			buffer_write_output3.close();
			absolute_write_output3.close();
		}

		catch (

		FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {

		}

	}

	public static TreeNode<Array_list> getSet1() {
		return root;
	}

	private static String createIndent(int depth) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < depth - 1; i++) {
			sb.append(" | ");
		}
		return sb.toString();

	}

}
