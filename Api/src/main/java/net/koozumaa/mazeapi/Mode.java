package net.koozumaa.mazeapi;

public enum Mode{
    NORMAL{
        public String toString(){
            return this.name();
        }
    },
    SLIM3x3{
        public String toString(){
            return this.name();
        }
    },
    WORLDGEN{
        public String toString(){return this.name();}
    },
    CUSTOM{
        public String toString(){return this.name();}

        public int getLength() {
            return length;
        }

        public int length = 1;
    },
    EPIC{
        public String toString(){return this.name();}
    }
}
