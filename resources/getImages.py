
# coding: utf-8

# In[27]:


import urllib.request


# In[36]:


f = open('images.txt', 'r')
lines = f.readlines()

url = 'http://zephyr.sista.arizona.edu/learn2cal/images/'

files = []
calories = []

for line in lines:
    line = line.replace('\n', '')
    file, calorie = line.split(',')
    
    files.append(file)
    calories.append(calorie)
    
    try:
        urllib.request.urlretrieve(url+file, file)
    except:
        print('error: ', file)

f.close()


# In[37]:


print (calories)

