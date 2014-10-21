import simulation.participants.interfaces.IBehaviour as IBehaviour
from random import choice

class Behaviour(IBehaviour):
    def __init__(self):
        pass
        
    def setParticipant(self, car):
        self._car = car
        self._maxVelocity = 90.0
        self._intervalDivider = (self._maxVelocity/9) #144 muss noch durch max velocity ersetzt werden
        self._tempPVelocity = 0
        
    def update(self, vision):
        """ all driving stuff """
        vision = vision.getData()
        car = self._car
        
        # random stiring
        directions = ["LEFT", "AHEAD", "RIGHT"]
                
        velocity = car.getVelocity()
        #print vision
        p = vision["NextParticipant"]
        t = vision["NextProperties"]
        v = vision["VisibleParticipants"]
        
        #[{u'Position': [6.633134068189738e-15, 36.109100341796875], u'AirDistance': 36.109100341796875, u'Direction': NORTH, u'Velocity': 31.26275634765625, u'Type': u'LIMOBLUE'}]
        #Prototype
        speed = True        
        
        #if t:
         #   #Wenn eine Kreuzung in sicht ist
         #   if 'crossing' in t[0]["Properties"]:
         #       if 'begin' in t[0]["Properties"]['crossing'] and t[0]["Distance"] > 15:
         #           pass
         #       elif 'begin' in t[0]["Properties"]['crossing'] and t[0]["Distance"] <= 15:
         #           car.decelerate(1)
         #       speed = False
        
        flag = False
        if v:
            for d in v:
                if d['AirDistance'] < 30: #Uebergangsloesung
                    flag = True
        if flag:
            car.steer('RIGHT')
        else:
            car.steer(choice(directions))
            
        if t:

            if 'curve' in t[0]['Properties']:
                dtc = t[0]['Distance']
                if dtc < 40 and velocity > 40:
                    speed = False
                    car.decelerate(0.4)

#             if "crossing" in t[0]["Properties"]:
#                 #Wenn eine Kreuzung in sicht ist
#                 if 'begin' in t[0]["Properties"]['crossing'] and t[0]["Distance"] > 15:
#                     if not p:
#                         speedDiff = 
#                     else:
#                         speedDiff = velocity - p["Velocity"]
#                     dValue = abs(1 - ((speedDiff / self._intervalDivider) / 10))
#                     print dValue
#                     car.decelerate(dValue)
#                 elif 'begin' in t[0]["Properties"]['crossing'] and t[0]["Distance"] <= 15:
#                     car.decelerate(1)

        
        if p:
            speedOfP = p["Velocity"]
            d = float(p["Distance"])
            move = self._tempPVelocity - speedOfP
            aV = velocity
            bV = speedOfP
            speedDiff = aV - bV;
            
            #flag = True
            #+move, Vorderes Auto beschleunigt
            
            if t and v:
                if t[0]["Distance"] < 5:
                    flag = True
            if t:
                if t[0]["Distance"] > 60:
                    pass
                elif t[0]["Distance"] <= 60 and aV > 80:
                    car.decelerate(0.8)
                elif t[0]["Distance"] <=60 and aV < 40:
                    pass
            elif move < 0 and aV < bV:
                car.accelerate(0.3)
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
                car.decelerate(0.8)
            elif move > 0 and aV > bV and d <= 60:
                car.decelerate(1)
            #=move
            elif move == 0 and aV > bV and d > 80:
                car.decelerate(0.25)
            elif move == 0 and aV > bV and d <= 80 and d > 60:
                car.decelerate(0.8)
            elif move == 0 and aV > bV and d <= 60:
                car.decelerate(1)
            elif move == 0 and aV == bV:
                pass
            
            #vorderes auto steht
            elif bV == 0 and aV > 0 and aV < 20 and d > 50:
                pass
            elif bV == 0 and aV > 0 and aV < 20 and d <= 50:
                car.decelerate(1)
            elif bV == 0 and aV > 20:
                dValue = abs(1 - ((speedDiff / self._intervalDivider) / 10))
                car.decelerate(dValue)
            
            #if flag:
            #    car.steer("RIGHT")
            #else:
            #    car.steer(choice(directions))
            speed = False
            self._tempPVelocity = speedOfP 
        
        if(speed and velocity < self._maxVelocity):
            # accelerate depending on driver type
            car.accelerate(0.5)