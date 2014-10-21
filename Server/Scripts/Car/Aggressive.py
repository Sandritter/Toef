import simulation.participants.interfaces.IBehaviour as IBehaviour
from random import choice

class Behaviour(IBehaviour):
    def __init__(self):
        pass
        
    def setParticipant(self, car):
        self._car = car
        self._maxVelocity = 140.0
        self._intervalDivider = (self._maxVelocity/9)
        self._tempPVelocity = 0
        
    def update(self, vision):
        """ all driving stuff """
        vision = vision.getData()
        car = self._car
        
        
        # random stiring
        directions = ["LEFT", "AHEAD", "RIGHT"]
        car.steer(choice(directions))
                
        velocity = car.getVelocity()
        #print vision
        p = vision["NextParticipant"]
        t = vision["NextProperties"]
        if(t):
            dtc = t[0]["Distance"]
        #dtc = t["Distance"]
        #print dtc
        #Prototype
        speed = True
        if(p):
            speedOfP = p["Velocity"]
            d = float(p["Distance"])
            move = self._tempPVelocity - speedOfP
            aV = velocity
            bV = speedOfP
            speedDiff = aV - bV;
            
            #+move, Vorderes Auto beschleunigt
            if move < 0 and aV < bV:
                car.accelerate(0.4)
            elif move < 0 and aV > bV:
                car.decelerate(0.4)
                
            #-move
            elif move > 0 and aV < bV and d > 80:
                car.accelerate(0.15)
            elif move > 0 and aV < bV and d <= 80 and d > 50:
                pass
            elif move > 0 and aV < bV and d <= 50:
                car.decelerate(1)
            elif move > 0 and aV > bV and d > 80:
                car.decelerate(0.25)
            elif move > 0 and aV > bV and d > 60 and d <= 80:
                car.decelerate(0.7)
            elif move > 0 and aV > bV and d <= 60:
                car.decelerate(1)
                
            #=move
            elif move == 0 and aV > bV and d > 80:
                car.decelerate(0.25)
            elif move == 0 and aV > bV and d <= 80 and d > 60:
                car.decelerate(0.7)
            elif move == 0 and aV > bV and d <= 60:
                car.decelerate(1)
            elif move == 0 and aV == bV:
                pass
            
            #vorderes auto steht
            elif bV == 0 and aV > 0 and aV < 20 and d > 35:
                pass
            elif bV == 0 and aV > 0 and aV < 20 and d <= 35:
                car.decelerate(1)
            elif bV == 0 and aV > 20:
                dValue = abs(1 - ((speedDiff / self._intervalDivider) / 10))
                car.decelerate(dValue)
            
            
            speed = False
            self._tempPVelocity = speedOfP 
        
        if(speed and velocity < self._maxVelocity):
            # accelerate depending on driver type
            car.accelerate(0.7)
                
                
                
            
        