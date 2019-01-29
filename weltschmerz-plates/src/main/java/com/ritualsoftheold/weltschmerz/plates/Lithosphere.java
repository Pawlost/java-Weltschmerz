package com.ritualsoftheold.weltschmerz.plates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Lithosphere {
    private static final float SQRDMD_ROUGHNESS = 0.5f;
    private static final float BUOYANCY_BONUS_X = 3;
    private static final int MAX_BUOYANCY_AGE = 20;
    private static final float MULINV_MAX_BUOYANCY_AGE = 1.0f / MAX_BUOYANCY_AGE;

    private static final float RESTART_ENERGY_RATIO = 0.15f;
    private static final float RESTART_SPEED_LIMIT = 2.0f;
    private static final int NO_COLLISION_TIME_LIMIT = 10;

    private static final float CONTINENTAL_BASE = 1.0f;
    private static final float OCEANIC_BASE = 0.1f;

    private static final float FLT_EPSILON = 1;
    private static final boolean BOOL_REGENERATE_CRUST = true;


    private static float sea_threshold = 0.5f;
    private static float th_step = 0.5f;

    private float[] hmap;
    private int[] imap;
    private float peak_Ek;
    private int last_coll_count;
    private int hmpat = 0;
    private Plate[] plates;
    private int aggr_overlap_abs;
    private float aggr_overlap_rel;
    private int cycle_count = 0;
    private float erosion_period;
    private float folding_ratio;
    private int iter_count = 0;
    private int map_side;
    private int max_cycles;
    private int num_plates;

    private ArrayList<ArrayList<PlateCollision>> collisions;
    private ArrayList<ArrayList<PlateCollision>> subductions;

    public Lithosphere(int map_side_length, float sea_level, int erosion_period,
                       float folding_ratio, int aggr_ratio_abs,
                       float aggr_ratio_rel, int num_cycles) {

        this.aggr_overlap_abs = aggr_ratio_abs;
        this.aggr_overlap_rel = aggr_ratio_rel;
        this.erosion_period = erosion_period;
        this.folding_ratio = folding_ratio;
        map_side = map_side_length + 1;
        max_cycles = num_cycles;

        collisions = new ArrayList<>();
        subductions = new ArrayList<>();

        int area = map_side * map_side;
        float[] tmp = new float[area];

        tmp = Calculations.sqrdmd(tmp, map_side, SQRDMD_ROUGHNESS);


       /* float lowest = tmp[0], highest = tmp[0];
        for (int i = 1; i < tmp.length; i++) {
            lowest = lowest < tmp[i] ? lowest : tmp[i];
            highest = highest > tmp[i] ? highest : tmp[i];
        }



        for (int i = 0; i < tmp.length; ++i) {// Scale to [0 ... 1]
                tmp[i] = (tmp[i] - lowest) / (highest - lowest);
            }
            */

        // Find the actual value in height map that produces the continent-sea
        // ratio defined be "sea_level".


        while (th_step > 0.01) {
            int count = 0;
            for (int i = 0; i < area; ++i) {
                count += (tmp[i] < sea_threshold ? 1 : 0);
            }
            th_step *= 0.5;
            if (count / (float) area < sea_level) {
                sea_threshold += th_step;
            } else {
                sea_threshold -= th_step;
            }
        }
        sea_level = sea_threshold;

        for (int i = 0; i < area; ++i) // Genesis 1:9-10.
        {
//		if (tmp[i] < sea_level)
//			tmp[i] /= sea_level;
//		else
//			tmp[i] = 1.0+(tmp[i] - sea_level) * 20;
            tmp[i] = (tmp[i] > sea_level ? 1.0f:0.0f) *
                    (tmp[i] + CONTINENTAL_BASE) +
                    (tmp[i] <= sea_level ? 1f:0f) * OCEANIC_BASE;
        }

        // Scalp the +1 away from map side to get a power of two side length!
        // Practically only the redundant map edges become removed.
        --map_side;
        hmap = new float[map_side * map_side];
        Arrays.fill(hmap, 0);
        hmap = Arrays.copyOf(tmp, hmap.length);
        imap = new int[map_side * map_side];
    }

   public void createPlates(int num_plates) {
       int map_area = map_side * map_side;
       this.num_plates = num_plates;

       ArrayList<PlateCollision> vec = new ArrayList<>();

       for (int i = 0; i < num_plates; ++i) {
           collisions.add(vec);
           subductions.add(vec);
       }

       // Initialize "Free plate center position" lookup table.
       // This way two plate centers will never be identical.
       for (int i = 0; i < map_area; ++i)
           imap[i] = i;
       Random random = new Random();
       // Select N plate centers from the global map.
       PlateArea[] area = new PlateArea[num_plates];
       Arrays.fill(area, new PlateArea());
       for (int i = 0; i < num_plates; ++i) {
           // Randomly select an unused plate origin.
           int p = imap[random.nextInt(map_area) % (map_area - i)];
           int y = p / map_side;
           int x = p - y * map_side;

           area[i].lft = area[i].rgt = x; // Save origin...
           area[i].top = area[i].btm = y;
           area[i].wdt = area[i].hgt = 1;
           area[i].border.add(p); // ...and mark it as border.

           // Overwrite used entry with last unused entry in array.
           imap[p] = imap[map_area - i - 1];
       }

       int[] owner = imap; // Create an alias.
       Arrays.fill(owner, 255);
       // "Grow" plates from their origins until surface is fully populated.
       boolean max_border = true;
       while (max_border)
           for (int i = 0; i < num_plates; ++i) {
               int N = area[i].border.size();
               max_border = 0 > N;

               if (N == 0)
                   continue;

               int j = random.nextInt(N) % N;
               int p = area[i].border.get(j);
               int cy = p / map_side;
               int cx = p - cy * map_side;

               int lft = cx > 0 ? cx - 1 : map_side - 1;
               int rgt = cx < map_side - 1 ? cx + 1 : 0;
               int top = cy > 0 ? cy - 1 : map_side - 1;
               int btm = cy < map_side - 1 ? cy + 1 : 0;

               int n = top * map_side + cx; // North.
               int s = btm * map_side + cx; // South.
               int w = cy * map_side + lft; // West.
               int e = cy * map_side + rgt; // East.

               if (owner[n] >= num_plates) {
                   owner[n] = i;
                   area[i].border.add(n);

                   if (area[i].top == ((top + 1) & (map_side - 1))) {
                       area[i].top = top;
                       area[i].hgt++;
                   }
               }

               if (owner[s] >= num_plates) {
                   owner[s] = i;
                   area[i].border.add(s);

                   if (btm == ((area[i].btm + 1) & (map_side - 1))) {
                       area[i].btm = btm;
                       area[i].hgt++;
                   }
               }

               if (owner[w] >= num_plates) {
                   owner[w] = i;
                   area[i].border.add(w);

                   if (area[i].lft == ((lft + 1) & (map_side - 1))) {
                       area[i].lft = lft;
                       area[i].wdt++;
                   }
               }

               if (owner[e] >= num_plates) {
                   owner[e] = i;
                   area[i].border.add(e);

                   if (rgt == ((area[i].rgt + 1) & (map_side - 1))) {
                       area[i].rgt = rgt;
                       area[i].wdt++;
                   }
               }

               // Overwrite processed point with unprocessed one.
               area[i].border.set(j, area[i].border.get(0));
               area[i].border.remove(0);
           }

           plates = new Plate[num_plates];

           // Extract and create plates from initial terrain.
           for (int i = 0; i < num_plates; ++i) {
               area[i].wdt = area[i].wdt < map_side ? area[i].wdt : map_side - 1;
               area[i].hgt = area[i].hgt < map_side ? area[i].hgt : map_side - 1;

               int x0 = area[i].lft;
               int x1 = 1 + x0 + area[i].wdt;
               int y0 = area[i].top;
               int y1 = 1 + y0 + area[i].hgt;
               int width = x1 - x0;
               int height = y1 - y0;
               float[] plt = new float[width * height];

//		printf("plate %u: (%u, %u)x(%u, %u)\n", i, x0, y0, width,
//			height);

               // Copy plate's height data from global map into local map.
               for (int y = y0, j = 0; y < y1; ++y)
                   for (int x = x0; x < x1; ++x, ++j) {
                       int k = (y & (map_side - 1)) * map_side +
                               (x & (map_side - 1));
                       plt[j] = hmap[k] * (owner[k] == i ? 1 : 0);
                   }

               // Create plate.
               plates[i] = new Plate(plt, width, height, x0, y0, i, map_side);
           }

           iter_count = num_plates + MAX_BUOYANCY_AGE;
           peak_Ek = 0;
           last_coll_count = 0;
   }
    public int getPlateCount() {
        return num_plates;
    }

    public float[] getTopography() {

        return Arrays.copyOf(hmap, hmap.length);
    }

    public void update() {
        float totalVelocity = 0;
        float systemKineticEnergy = 0;

        for (int i = 0; i < num_plates; ++i) {
            totalVelocity += plates[i].getVelocity();
            systemKineticEnergy += plates[i].getMomentum();
        }

        if (systemKineticEnergy > peak_Ek)
            peak_Ek = systemKineticEnergy;

//	printf("%f > %f, ", totalVelocity, RESTART_SPEED_LIMIT);
//	printf("%f/%f = %f > %f\n", systemKineticEnergy, peak_Ek,
//		systemKineticEnergy / peak_Ek, RESTART_ENERGY_RATIO);

        // If there's no continental collisions during past iterations,
        // then interesting activity has ceased and we should restart.
        // Also if the simulation has been going on for too long already,
        // restart, because interesting stuff has most likely ended.
        if (totalVelocity < RESTART_SPEED_LIMIT ||
                systemKineticEnergy / peak_Ek < RESTART_ENERGY_RATIO ||
                last_coll_count > NO_COLLISION_TIME_LIMIT ||
                iter_count > 600) {
            restart();
            return;
        }

        int map_area = map_side * map_side;
        int[] prev_imap = imap;
        int[] amap = new int[map_area];
        imap = new int[map_area];

        // Realize accumulated external forces to each plate.
        for (int i = 0; i < num_plates; ++i) {
            // Dont't do it yet.. There's problems with the index map...
            if (false && plates[i].isEmpty())
            {
                plates[i] = null;
                plates[i] = plates[--num_plates];
                --i;

                continue;
            }

            plates[i].resetSegments();

            if (erosion_period > 0 && iter_count % erosion_period == 0)
                plates[i].erode(CONTINENTAL_BASE);

            plates[i].move();
        }

//	static size_t max_collisions = 0;	// DEBUG!!!
        int oceanic_collisions = 0;
        int continental_collisions = 0;

        // Update height and plate index maps.
        // Doing it plate by plate is much faster than doing it index wise:
        // Each plate's map's memory area is accessed sequentially and only
        // once as opposed to calculating "num_plates" indices within plate
        // maps in order to find out which plate(s) own current location.
        Arrays.fill(hmap, 0);
        Arrays.fill(imap, 255);


        for (int i = 0; i < num_plates; ++i) {
        int x0 = (int) plates[i].getLeft();
        int y0 = (int) plates[i].getTop();
        int x1 = (int) x0 + plates[i].getWidth();
        int y1 = y0 + plates[i].getHeight();

        float[] this_map =  plates[i].getMap();
        int[] this_age =  plates[i].getAge();

            // Copy first part of plate onto world map.
            for (int y = y0, j = 0; y < y1; ++y)
                for (int x = x0; x < x1; ++x, ++j) {
        int x_mod = x & (map_side - 1);
        int y_mod = y & (map_side - 1);

        int k = y_mod * map_side + x_mod;

                    if (this_map[j] < 2 * FLT_EPSILON) // No crust here...
                        continue;

                    if (imap[k] >= num_plates) // No one here yet?
                    {
                        // This plate becomes the "owner" of current location
                        // if it is the first plate to have crust on it.
                        hmap[k] = this_map[j];
                        imap[k] = i;
                        amap[k] = this_age[j];

                        continue;
                    }

                    // DO NOT ACCEPT HEIGHT EQUALITY! Equality leads to subduction
                    // of shore that 's barely above sea level. It's a lot less
                    // serious problem to treat very shallow waters as continent...
        boolean prev_is_oceanic = hmap[k] < CONTINENTAL_BASE;
		boolean this_is_oceanic = this_map[j] < CONTINENTAL_BASE;

		int prev_timestamp = plates[imap[k]].getCrustTimestamp(x_mod, y_mod);
                int this_timestamp = this_age[j];
                boolean prev_is_bouyant = (hmap[k] > this_map[j]) |
                            ((hmap[k] + 2 * FLT_EPSILON > this_map[j]) &
                                    (hmap[k] < 2 * FLT_EPSILON + this_map[j]) &
                                    (prev_timestamp >= this_timestamp));

                    // Handle subduction of oceanic crust as special case.
                    if (this_is_oceanic & prev_is_bouyant) {
                        // This plate will be the subducting one.
                        // The level of effect that subduction has
                        // is directly related to the amount of water
                        // on top of the subducting plate.
             float sediment = OCEANIC_BASE * (CONTINENTAL_BASE - this_map[j]) / CONTINENTAL_BASE;

                        // Save collision to the receiving plate's list.
                        PlateCollision coll = new PlateCollision(i, x_mod, y_mod, sediment);
                        subductions.get(imap[k]).add(coll);
                        ++oceanic_collisions;

                        // Remove subducted oceanic lithosphere from plate.
                        // This is crucial for
                        // a) having correct amount of colliding crust (below)
                        // b) protecting subducted locations from receiving
                        //    crust from other subductions/collisions.
                        plates[i].setCrust(x_mod, y_mod, this_map[j] -
                                OCEANIC_BASE, this_timestamp);

                        if (this_map[j] <= 0)
                            continue; // Nothing more to collide.
                    } else if (prev_is_oceanic) {
             float sediment = OCEANIC_BASE * (CONTINENTAL_BASE - hmap[k]) / CONTINENTAL_BASE;

                        PlateCollision coll = new PlateCollision (imap[k], x_mod, y_mod, sediment);
                        subductions.get(i).add(coll);
                        ++oceanic_collisions;

                        plates[imap[k]].setCrust(x_mod, y_mod, hmap[k] -
                                OCEANIC_BASE, prev_timestamp);
                        hmap[k] -= OCEANIC_BASE;

                        if (hmap[k] <= 0) {
                            imap[k] = i;
                            hmap[k] = this_map[j];
                            amap[k] = this_age[j];

                            continue;
                        }
                    }

                    // Record collisions to both plates. This also creates
                    // continent segment at the collided location to plates.
                    int this_area = plates[i].addCollision(x_mod, y_mod);
                    int prev_area = plates[imap[k]].addCollision(x_mod, y_mod);

                    // At least two plates are at same location.
                    // Move some crust from the SMALLER plate onto LARGER one.
                    if (this_area < prev_area) {
                        PlateCollision coll = new PlateCollision(imap[k], x_mod, y_mod, this_map[j] * folding_ratio);

                        // Give some...
                        hmap[k] += coll.crust;
                        plates[imap[k]].setCrust(x_mod, y_mod, hmap[k],
                                this_age[j]);

                        // And take some.
                        plates[i].setCrust(x_mod, y_mod,  (float) (this_map[j] * (1.0 - folding_ratio)), this_age[j]);

                        // Add collision to the earlier plate's list.
                        collisions.get(i).add(coll);
                        ++continental_collisions;
                    } else {
                        PlateCollision coll = new PlateCollision (i, x_mod, y_mod,
                                hmap[k] * folding_ratio);

                        plates[i].setCrust(x_mod, y_mod,
                                this_map[j] + coll.crust, amap[k]);

                        plates[imap[k]].setCrust(x_mod, y_mod, (float) (hmap[k]
                                * (1.0 - folding_ratio)), amap[k]);

                        collisions.get(imap[k]).add(coll);
                        ++continental_collisions;

                        // Give the location to the larger plate.
                        hmap[k] = this_map[j];
                        imap[k] = i;
                        amap[k] = this_age[j];
                    }
                }
        }

//	size_t total_collisions = oceanic_collisions + continental_collisions;
//	if (total_collisions > max_collisions)
//		max_collisions = total_collisions;
//	printf("%5u + %5u = %5u collisions (%f %%) (max %5u (%f %%)). %c\n",
//		oceanic_collisions, continental_collisions, total_collisions,
//		(float)total_collisions / (float)(map_side * map_side),
//		max_collisions, (float)max_collisions /
//		(float)(map_side * map_side), '+' + (2 & -(iter_count & 1)));

        // Update the counter of iterations since last continental collision.
        last_coll_count = (last_coll_count + 1) & -(continental_collisions == 0 ? 1:0);

        for (int i = 0; i < num_plates; ++i) {
            for (int j = 0; j < subductions.get(i).size(); ++j) {
        PlateCollision coll = subductions.get(i).get(j);

                if (i == coll.index) {
                    //TODO exeption
                }

                // Do not apply friction to oceanic plates.
                // This is a very cheap way to emulate slab pull.
                // Just perform subduction and on our way we go!
                plates[i].addCrustBySubduction(
                        coll.wx, coll.wy, coll.crust, iter_count,
                        plates[coll.index].getVelX(),
                        plates[coll.index].getVelY());
            }

            subductions.get(i).clear();
        }

        for (int i = 0; i < num_plates; ++i) {
            for (int j = 0; j < collisions.get(i).size(); ++j) {
         PlateCollision coll = collisions.get(i).get(j);

                if (i == coll.index) {
                    //TODO exeption
                }

                // Collision causes friction. Apply it to both plates.
                plates[i].applyFriction(coll.crust);
                plates[coll.index].applyFriction(coll.crust);
//			hmap[coll.wy * map_side + coll.wx] = 0;

                int coll_count_i = plates[i].getCollisionCount(coll.wx, coll.wy);
                float coll_ratio_i =  plates[i].getCollisionRatio(coll.wx, coll.wy);


                int coll_count_j = plates[coll.index].getCollisionCount(coll.wx, coll.wy);
                float coll_ratio_j =  plates[coll.index].getCollisionRatio(coll.wx, coll.wy);


                // Find the minimum count of collisions between two
                // continents on different plates.
                // It's minimum because large plate will get collisions
                // from all over where as smaller plate will get just
                // a few. It's those few that matter between these two
                // plates, not what the big plate has with all the
                // other plates around it.
                int coll_count = coll_count_i;
                coll_count -= (coll_count - coll_count_j) &
                        -(coll_count > coll_count_j ? 1:0);

                // Find maximum amount of collided surface area between
                // two continents on different plates.
                // Like earlier, it's the "experience" of the smaller
                // plate that matters here.
                float coll_ratio = coll_ratio_i;
                coll_ratio += (coll_ratio_j - coll_ratio) *
                        (coll_ratio_j > coll_ratio ? 1:0);

//			printf("min(%u, %u) = %u, max(%f, %f) = %f\n",
//				coll_count_i, coll_count_j, coll_count,
//				coll_ratio_i, coll_ratio_j, coll_ratio);

                if ((coll_count > aggr_overlap_abs) |
                        (coll_ratio > aggr_overlap_rel)) {
                    float amount = plates[i].aggregateCrust(
                            plates[coll.index],
                            coll.wx, coll.wy);

                    // Calculate new direction and speed for the
                    // merged plate system, that is, for the
                    // receiving plate!
                    plates[coll.index].collide(plates[i],
                            coll.wx, coll.wy, amount);
                }
            }

            collisions.get(i).clear();
        }

        // Fill divergent boundaries with new crustal material, molten magma.
        if(BOOL_REGENERATE_CRUST) {
            for (int y = 0, i = 0; y < map_side; ++y){
                for (int x = 0; x < map_side; ++x, ++i){
                    if (imap[i] >= num_plates) {
                        // The owner of this new crust is that neighbour plate
                        // who was located at this point before plates moved.
                        imap[i] = prev_imap[i];

                        if (imap[i] >= num_plates) {
                            //TODO expetion
                        }

                        // If this is oceanic crust then add buoyancy to it.
                        // Magma that has just crystallized into oceanic crust
                        // is more buoyant than that which has had a lot of
                        // time to cool down and become more dense.
                        amap[i] = iter_count;
                        hmap[i] = OCEANIC_BASE * BUOYANCY_BONUS_X;

                        plates[imap[i]].setCrust(x, y, OCEANIC_BASE,
                                iter_count);
                    }
                }
                        // DEBUG!
//			size_t lx = x, ly = y;
//			plates[imap[i]]->getMapIndex(&lx, &ly);
//			size_t px = (size_t) plates[imap[i]]->left + lx;
//			size_t py = (size_t) plates[imap[i]]->top + ly;
//
//			if ((py & (map_side - 1)) * map_side +
//				(px & (map_side - 1)) != i)
//			{
//				puts("Added sea floor to odd place!");
//				exit(1);
//			}
                    }
//		else if (hmap[i] <= 0)
//		{
//			puts("Occupied point has no land mass!");
//			exit(1);
//		}
        }

        // Add some "virginity buoyancy" to all pixels for a visual boost! :)
        for (int i = 0; i < map_area; ++i) {
            // Calculate the inverted age of this piece of crust.
            // Force result to be minimum between inv. age and
            // max buoyancy bonus age.
            int crust_age = iter_count - amap[i];
            crust_age = MAX_BUOYANCY_AGE - crust_age;
            crust_age &= -(crust_age <= MAX_BUOYANCY_AGE ? 1:0);

            hmap[i] += (hmap[i] < CONTINENTAL_BASE  ? 1:0) * BUOYANCY_BONUS_X *
                    OCEANIC_BASE * crust_age * MULINV_MAX_BUOYANCY_AGE;
        }

/*	size_t i = 0;
	const size_t x0 = (size_t)plates[i]->getLeft();
	const size_t y0 = (size_t)plates[i]->getTop();
	const size_t x1 = x0 + plates[i]->getWidth();
	const size_t y1 = y0 + plates[i]->getHeight();

	const float*  this_map;
	const size_t* this_age;
	plates[i]->getMap(&this_map, &this_age);

	// Show only plate[0]'s segments, draw everything else dark blue.
	if (iter_count < 300)
	for (size_t y = y0, j = 0; y < y1; ++y)
	  for (size_t x = x0; x < x1; ++x, ++j)
	  {
		const size_t x_mod = x & (map_side - 1);
		const size_t y_mod = y & (map_side - 1);

		const size_t k = y_mod * map_side + x_mod;

		if (this_map[j] < 2 * FLT_EPSILON) // No crust here...
		{
			hmap[k] = 4*FLT_EPSILON;
			continue;
		}

		float Q = (plates[i]->segment[j] < plates[i]->seg_data.size());
		hmap[k] = (this_map[j] * Q);
	  }*/
        ++iter_count;
    }

    public void restart()

    {
        int map_area = map_side * map_side;
        int[] amap = new int[map_area];

        cycle_count += max_cycles > 0 ? 1:0; // No increment if running for ever.
        if (cycle_count > max_cycles)
            return;

        // Update height map to include all recent changes.
        Arrays.fill(hmap, 0);

        for (int i = 0; i < num_plates; ++i) {
        int x0 = (int) plates[i].getLeft();
        int y0 = (int) plates[i].getTop();
        int x1 = x0 + plates[i].getWidth();
        int y1 = y0 + plates[i].getHeight();

        float[] this_map =  plates[i].getMap();
        int[] this_age =  plates[i].getAge();

            // Copy first part of plate onto world map.
            for (int y = y0, j = 0; y < y1; ++y)
                for (int x = x0; x < x1; ++x, ++j) {
        int x_mod = x & (map_side - 1);
        int y_mod = y & (map_side - 1);

                    hmap[y_mod * map_side + x_mod] += this_map[j];
                    amap[y_mod * map_side + x_mod] = this_age[j];
                }
        }

        // Delete plates.;
        plates = null;

        // create new plates IFF there are cycles left to run!
        // However, if max cycle count is "ETERNITY", then 0 < 0 + 1 always.
        if (cycle_count < max_cycles + max_cycles) {
            amap = null;
            createPlates(num_plates);
            return;
        } else
            num_plates = 0;

        // Add some "virginity buoyancy" to all pixels for a visual boost.
        for (int i = 0; i < (BUOYANCY_BONUS_X > 0 ? 1:0) * map_area; ++i) {
            int crust_age = iter_count - amap[i];
            crust_age = MAX_BUOYANCY_AGE - crust_age;
            crust_age &= -(crust_age <= MAX_BUOYANCY_AGE ? 1:0);

            hmap[i] += (hmap[i] < CONTINENTAL_BASE ? 1:0) * BUOYANCY_BONUS_X *
                    OCEANIC_BASE * crust_age * MULINV_MAX_BUOYANCY_AGE;
        }

        // This is the LAST cycle! Add some random noise to the map.
        int A = (map_side + 1) * (map_side + 1);
        float[] tmp = new float[A];

        Arrays.fill(tmp, 0);

        tmp = Calculations.sqrdmd(tmp, map_side + 1, SQRDMD_ROUGHNESS);

        // Shrink the fractal map by 1 pixel from right and bottom.
        // This makes it same size as lithosphere's height map.

        float t_lowest = tmp[0], t_highest = tmp[0];
        float h_lowest = hmap[0], h_highest = hmap[0];
        for (int i = 1; i < map_area; ++i) {
            t_lowest = t_lowest < tmp[i] ? t_lowest : tmp[i];
            t_highest = t_highest > tmp[i] ? t_highest : tmp[i];

            h_lowest = h_lowest < hmap[i] ? h_lowest : hmap[i];
            h_highest = h_highest > hmap[i] ? h_highest : hmap[i];
        }

        for (int i = 0; i < map_area; ++i) {
            // Scale to range [0, 1].
            tmp[i] = (tmp[i] - t_lowest) / (t_highest - t_lowest);

            if (hmap[i] > CONTINENTAL_BASE) {
                hmap[i] += tmp[i] * 2;
//			hmap[i] = CONTINENTAL_BASE +
//				0.5 * (tmp[i] - 0.5) * CONTINENTAL_BASE +
//				0.1 * tmp[i] * (h_highest - CONTINENTAL_BASE) +
//				0.9 * (hmap[i] - CONTINENTAL_BASE);
            }else{
                hmap[i] = (float) (0.8 * hmap[i] + 0.2 * tmp[i] * CONTINENTAL_BASE);
            }
        }
    }
}
class PlateArea{
    public ArrayList<Integer> border;
    public Integer btm;
    public Integer lft;
    public Integer rgt;
    public Integer top;
    public Integer wdt;
    public Integer hgt;
    public PlateArea(){
        border = new ArrayList<>();
    }
}

class PlateCollision
{
    public int index;
    public int wx,wy;
    public float crust;
   public PlateCollision(int index, int x, int y, float z){
        this.index = index;
        this.wx = x;
        this.wy = y;
        crust = z;
   }
}