import os
import importlib

def get_filepaths(directory):
    """
    This function will generate the file names in a directory 
    tree by walking the tree either top-down or bottom-up. For each 
    directory in the tree rooted at directory top (including top itself), 
    it yields a 3-tuple (dirpath, dirnames, filenames).
    """
    file_paths = []  # List which will store all of the full filepaths.

    # Walk the tree.
    for root, directories, files in os.walk(directory):
        for filename in files:
            # Join the two strings in order to form the full filepath.
            #print("root",root)
            filepath=root.replace("\\","/")
            #print("root", root)
            filepath=filepath+"/"+filename
            #filepath = os.path.join(root, filename)
            file_paths.append(filepath)  # Add it to the list.
            #module = importlib.import_module(filepath)
            #my_class = getattr(module, 'MyClass')
            #my_instance = my_class()
            #dir()
#           print("filepath : ",filepath," members are :",dir(module(filepath)))
            #print("filepath : ", filepath)
            file = open(filepath, "r+")
            variable=[]
            functionName=[]
            className=[]
            importModules=[]
            isNextWordfxn=False
            isNextWordclass=False
            isNextWordImport=False

            for word in file.read().split():
                if(isNextWordclass):
                 className.append(word)
                 isNextWordclass=False
                 #print(word)
                elif(isNextWordfxn):
                 functionName.append(word)
                 isNextWordfxn=False
                 #print(word)
                elif(isNextWordImport):
                 importModules.append(word)
                 isNextWordImport=False
                 #print(word)

                if (word == "def"):
                    isNextWordfxn = True
                    #print("true")
                elif (word == "class"):
                    isNextWordclass = True
                elif (word == "import"):
                    isNextWordImport = True


            print("File:",filepath,"Functions:",functionName)
            print("File:",filepath,"Classes:", className)
            print("File:",filepath,"Import:", importModules)
    return file_paths  # Self-explanatory.


# Run the above function and store its results in a variable.
full_file_paths = get_filepaths("E:/DR/tensorflow/python/ops")


#print(dir("E:/DR/tensorflow/python/ops/math_ops.py"))
#print(dir(a))
#print(dir("C:/Users/Puchu/PycharmProjects/MyFirstPythonProject/part3/AmericanFlag.py"))
#print(full_file_paths)