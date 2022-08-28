from pyvi import ViTokenizer, ViPosTagger
from pyvi import ViUtils
from pyvi import ViUtils


ViTokenizer.tokenize(u"Trường đại học bách khoa hà nội")

ViPosTagger.postagging(ViTokenizer.tokenize(u"Trường đại học Bách Khoa Hà Nội"))


ViUtils.remove_accents(u"Trường đại học bách khoa hà nội")


ViUtils.add_accents(u'truong dai hoc bach khoa ha noi')

print(ViTokenizer.tokenize("Trường kinh tế quốc dân hải phòng"))

print(ViPosTagger.postagging(ViTokenizer.tokenize("Trường kinh tế quốc dân hải phòng")))

print(ViUtils.remove_accents("Trường đại học bách khoa hà nội"))

print(ViUtils.add_accents('truong dai hoc bach khoa ha noi'))