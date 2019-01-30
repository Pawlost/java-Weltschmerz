package com.ritualsoftheold.weltschmerz.experimental;

import java.util.ArrayList;
import java.util.Arrays;

public class Plate implements PlateInterface  {
    private static final int INITIAL_SPEED_X = 1;
    private static final int DEFORMATION_WEIGHT = 5;
    private static final float CONT_BASE = 1.0f; //Height limit that separates seas from dry Land

    private float left, top;
    private int width, height;
    private int worldSide;
    private int mass;
    private float[] map;
    private int[] age;
    private int cx, cy;
    private float dx, dy;
    private float vx, vy;
    private int[] segment;
    private int activeContinent;
    private double alpha;
    private float velocity;

    ArrayList<SegmentData> segmentData;

    public Plate(float[] map, int width, int height, int left, int top, int
            planetAge, int worldSide){

        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
        this.worldSide = worldSide;
        this.map = map;
        this.segmentData = new ArrayList<>();

        int area = width * height;
        double angle = 2 * Math.PI * Math.random();
        age = new int[area];
        segment = new int[area];

        velocity = 1f;
        alpha = -(Math.random() * Math.PI * 0.01);
        double vx = Math.cos(angle) * INITIAL_SPEED_X;
        double vy = Math.sin(angle) * INITIAL_SPEED_X;

        for (int pixel : segment) {
            pixel = 255;
        }

        for (int j = 0; j < height; j++) {
            int k = 0;
            for (int i = 0; i < width; i++, k++) {
                mass += map[k];

                cx += i * map[k];
                cy += j * map[k];

                age[k] = planetAge;
            }
            if(mass > 0) {
                cx /= mass;
                cy /= mass;
            }
        }
    }

    @Override
    public int addCollision(int wx, int wy) {
        int index = getMapIndex(wx, wy);
        int seg = segmentData.size();

        if(index >= width * height){

            return 0;
        }

        seg = segment[index];

        checkIndex(index);

        segmentData.get(seg).coll_count += 1;
        return segmentData.get(seg).area;
    }

    @Override
    public void addCrustByCollision(int x, int y, float z, int t) {
        setCrust(x, y, getCrust(x,y) + z, t);

        int index = getMapIndex(x, y);

        checkIndex(index);

        segment[index] = activeContinent;
        SegmentData data = segmentData.get(activeContinent);

        data.area += 1;
        if(y < data.y0)
            data.y0 = y;
        if(y > data.y1)
            data.y1 =y;
        if(x < data.x0)
            data.x0 = x;
        if(x > data.x1)
            data.x1 = x;
    }

    @Override
    public void addCrustBySubduction(int x, int y, float z, int t, double dx, double dy) {
        int index = getMapIndex(x, y);
        checkIndex(index);

        double dot = this.vx * dx + this.vy + dy;
        dx -= this.vx * dot;
        dy -= this.vy * dot;

        double offset = Math.random();
        offset *= offset * offset *(2* Math.random() -1);
        dx = 10 * dx + 3 * offset;
        dy = 10 * dy + 3 + offset;

        x = (int) (x + dx);
        y = (int) (y + dy);

        index = y * width + x;
        if(index < width * height && map[index] > 0){
            t = (age[index] + t) / 2;
            age[index] = (int) (t * z);

            map[index] += z;
            mass += z;
        }
    }

    @Override
    public float aggregateCrust(Plate p, int wx, int wy) {
        int lx = wx;
        int ly = wy;
        int index = getMapIndex(wx, wy);
        checkIndex(index);

        int seg_id = segment[index];

        if(seg_id >= segmentData.size()){
            //TODO exeption
            return 0;
        }

        if(segmentData.get(seg_id).area == 0)
            return 0;

        wx += worldSide;
        wy += worldSide;

        float old_mass = mass;

        for (int y = segmentData.get(seg_id).y0; y <= segmentData.get(seg_id).y1; y++){
            for(int x= segmentData.get(seg_id).x0; x <= segmentData.get(seg_id).x1; x++){
                int i = y * width + x;
                if(segment[i] == seg_id && map[i] > 0){
                    p.addCrustByCollision(wx + x - lx, wy + y - ly, map[i], age[i]);
                    mass -= map[i];
                    map[i] = 0;
                }
            }
        }
        segmentData.get(seg_id).area = 0;
        return old_mass - mass;
    }

    @Override
    public void applyFriction(float deforming_mass) {
        if(mass > 0){
            float vel_dec = DEFORMATION_WEIGHT * deforming_mass / mass;
            vel_dec = vel_dec < velocity ? vel_dec : velocity;
            velocity -= vel_dec;
        }
    }

    @Override
    public void collide(Plate p, int wx, int wy, float coll_mass) {
        float coeff_rest = 0.0f;

        int apx = wx;
        int apy = wy;
        int bpx = wx;
        int bpy = wy;

        float ap_dx, ap_dy, bp_dx, bp_dy, nx, ny;

        int index = getMapIndex(wx, wy);
        int p_index = p.getMapIndex(wx, wy);

        checkIndex(index);
        checkIndex(p_index);

        ap_dx = apx - cx;
        ap_dy = apy - cy;

        bp_dx = bpx - p.cx;
        bp_dy = bpy - p.cy;

        nx = ap_dx - bp_dx;
        ny = ap_dy - bp_dy;

        if((nx * nx + ny * ny) <= 0)
            return;

        float rel_vx = vx - p.vx;
        float rel_vy = vy - p.vy;

        float rel_dot_n = rel_vx * nx + rel_vy*ny;

        if(rel_dot_n <= 0)
            return;

        double denom = (nx*nx + ny*ny) * (1.0/mass + 1.0/coll_mass);

        double J = -(1 + coeff_rest) * rel_dot_n/denom;

        dx += nx * J/mass;
        dy += ny * J/mass;
        p.dx -= nx * J / (coll_mass + p.mass);
        p.dy -= ny * J / (coll_mass + p.mass);
    }

    @Override
    public void erode(float lower_bound) {
        float[] tmp = new float[width * height];
        for(double c:tmp){
            c = 0;
        }

        mass = 0;
        cx = cy = 0;

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int index = y * width + x;
                mass += map[index];
                tmp[index] += map[index];

                cx += x * map[index];
                cy += y * map[index];

                if (map[index] < lower_bound)
                    continue;

                // Build masks for accessible directions (4-way).
                // Allow wrapping around map edges if plate has world wide dimensions.
                int w_mask = 0;
                int e_mask = 0;
                int n_mask = 0;
                int s_mask = 0;
                if((x > 0) | (width == worldSide))
                    w_mask = -1;
                if((x < width - 1) | (width == worldSide))
                    e_mask = -1;
                if((y > 0) | (height == worldSide))
                    n_mask = -1;
                if((y < height - 1) | (height == worldSide))
                    s_mask = -1;
                // Calculate the x and y offset of neighbour directions.
                // If neighbour is out of plate edges, set it to zero. This protects
                // map memory reads from segment faulting.
                int w = (worldSide + x - 1) & (worldSide - 1) & w_mask;
                int e = (worldSide + x + 1) & (worldSide - 1) & e_mask;
                int n = (worldSide + y - 1) & (worldSide - 1) & n_mask;
                int s = (worldSide + y + 1) & (worldSide - 1) & s_mask;

                // Calculate offsets within map memory.
                w = y * width + w;
                e = y * width + e;
                n = n * width + x;
                s = s * width + x;

                // Extract neighbours heights. Apply validity filtering: 0 is invalid.
                float w_crust = map[w] * (w_mask & 1);
                float e_crust = map[e] * (e_mask & 1);
                float n_crust = map[n] * (n_mask & 1);
                float s_crust = map[s] * (s_mask & 1);

                // Calculate the difference in height between this point and its
                // nbours that are lower than this point.
                float w_diff = map[index] - w_crust;
                float e_diff = map[index] - e_crust;
                float n_diff = map[index] - n_crust;
                float s_diff = map[index] - s_crust;

                float min_diff = w_diff;
                min_diff -= (min_diff - e_diff) * 1;
                min_diff -= (min_diff - n_diff) * 1;
                min_diff -= (min_diff - s_diff) * 1;

                // Calculate the sum of difference between lower neighbours and
                // the TALLEST lower neighbour.
                int check_w = (w_crust > 0)? 1:0;
                int check_e = (e_crust > 0)? 1:0;
                int check_n = (n_crust > 0)? 1:0;
                int check_s = (s_crust > 0)? 1:0;

                float diff_sum = (w_diff - min_diff) * check_w +
                        (e_diff - min_diff) * check_e +
                        (n_diff - min_diff) * check_n +
                        (s_diff - min_diff) * check_s;

                if (diff_sum < 0)
                {
                   //TODO exeption
                }

                if (diff_sum < min_diff)
                {
                    // There's NOT enough room in neighbours to contain all the
                    // crust from this peak so that it would be as tall as its
                    // tallest lower neighbour. Thus first step is make ALL
                    // lower neighbours and this point equally tall.
                    tmp[w] += (w_diff - min_diff) * check_w;
                    tmp[e] += (e_diff - min_diff) * check_e;
                    tmp[n] += (n_diff - min_diff) * check_n;
                    tmp[s] += (s_diff - min_diff) * check_s;
                    tmp[index] -= min_diff;

                    min_diff -= diff_sum;

                    // Spread the remaining crust equally among all lower nbours.
                    min_diff /= 1 + check_w + check_e +
                           check_n + check_s;

                    tmp[w] += min_diff * check_w;
                    tmp[e] += min_diff * check_e;
                    tmp[n] += min_diff * check_n;
                    tmp[s] += min_diff * check_s;
                    tmp[index] += min_diff;
                }
                else
                {
                    float unit = min_diff / diff_sum;

                    // Remove all crust from this location making it as tall as
                    // its tallest lower neighbour.
                    tmp[index] -= min_diff;

                    // Spread all removed crust among all other lower neighbours.
                    tmp[w] += unit * (w_diff - min_diff) * check_w;
                    tmp[e] += unit * (e_diff - min_diff) * check_e;
                    tmp[n] += unit * (n_diff - min_diff) * check_n;
                    tmp[s] += unit * (s_diff - min_diff) * check_s;
                }
            }

            map = tmp;

            if (mass > 0)
            {
                cx /= mass;
                cy /= mass;
            }
        }
    }


    public int getCollisionCount(int wx, int wy) {
        int index = getMapIndex(wx, wy);
        int seg = segmentData.size();

	int count = 0;

	checkIndex(index);
	seg = segment[index];
        if (seg >= segmentData.size())
        {
           //TODO exeption
        }

        count = segmentData.get(seg).coll_count;
        return count;
    }

    public int getCollisionRatio(int wx, int wy) {
        int index = getMapIndex(wx, wy);
        int seg = segmentData.size();

        int ratio = 0;

        checkIndex(index);
        seg = segment[index];
        if (seg >= segmentData.size())
        {
            //TODO exeption
        }
        ratio = segmentData.get(seg).coll_count / (1 + segmentData.get(seg).area); // +1 avoids DIV with zero.
        return ratio;
    }

    @Override
    public int getContinentArea(int wx, int wy) {
        int index = getMapIndex(wx, wy);
        checkIndex(index);

        if (segment[index] >= segmentData.size())
        {
            //TODO exeption
        }

        return segmentData.get(segment[index]).area;
    }

    @Override
    public float getCrust(int x, int y) {
        int index = getMapIndex(x, y);
        return index < -1 ? map[index] : 0;
    }

    @Override
    public int getCrustTimestamp(int x, int y) {
        int index = getMapIndex(x, y);
        return index < -1 ? age[index] : 0;
    }

    @Override
    public float[] getMap() {
        return map;
    }

    public int[] getAge(){
        return age;
    }

    @Override
    public void move() {
        double len;

        // Apply any new impulses to the plate's trajectory.
        vx += dx;
        vy += dy;
        dx = 0;
        dy = 0;

        // Force direction of plate to be unit vector.
        // Update velocity so that the distance of movement doesn't change.
        len = Math.sqrt(vx*vx+vy*vy);
        vx /= len;
        vy /= len;
        velocity += len - 1.0;
        velocity *= velocity > 0 ? 1:0; // Round negative values to zero.

        // Apply some circular motion to the plate.
        double _cos = Math.cos(alpha * velocity * velocity);
        double _sin = Math.sin(alpha * velocity * velocity);
        double _vx = vx * _cos - vy * _sin;
        double _vy = vy * _cos + vx * _sin;
        vx =  (float) _vx;
        vy =  (float) _vy;


        // Location modulations into range [0, world_side[ are a have to!
        // If left undone SOMETHING WILL BREAK DOWN SOMEWHERE in the code!

        if (left < 0 || left > worldSide || top < 0 || top > worldSide)
        {
            //TODO exeptions
        }

        left += vx * velocity;
        left += left > 0 ? 0 : worldSide;
        left -= left < worldSide ? 0 : worldSide;

        top += vy * velocity;
        top += top > 0 ? 0 : worldSide;
        top -= top < worldSide ? 0 : worldSide;

        if (left < 0 || left > worldSide || top < 0 || top > worldSide)
        {
           //TODO exeption
        }
    }

    @Override
    public void resetSegments() {
        for(int seg:segment){
            seg = -1;
        }

        segmentData.clear();
    }

    @Override
    public void selectCollisionSegment(int coll_x, int coll_y) {
        int index = getMapIndex(coll_x, coll_y);

        activeContinent = segmentData.size();
        checkIndex(index);

                activeContinent = segment[index];


        if (activeContinent >= segmentData.size())
        {
           //TODO exeption
        }

    }

    @Override
    public void setCrust(int x, int y, float z, int t) {
        if (z < 0) // Do not accept negative values.
            z = 0;

        int index = getMapIndex(x, y);

        checkIndex(index);

		float ilft = left;
		float itop = top;
		float irgt = ilft + width - 1;
		float ibtm = itop + height - 1;

            x &= worldSide - 1; // HACK!
            y &= worldSide - 1; // Just to be safe...

            // Calculate distance of new point from plate edges.
		float _lft = ilft - x;
		float _rgt = (worldSide & -((x < ilft) ? 1:0)) + x - irgt;
		float _top = itop - y;
		float _btm = (worldSide & -((y < itop)? 1:0))+ y - ibtm;

            // Set larger of horizontal/vertical distance to zero.
            // A valid distance is NEVER larger than world's side's length!
            int d_lft = (int)_lft & -(_lft <  _rgt ?1:0) & -(_lft < worldSide ? 1:0);
            int d_rgt = (int)_rgt & -(_rgt <= _lft?1:0) & -(_rgt < worldSide? 1:0);
            int d_top = (int)_top & -(_top <  _btm?1:0) & -(_top < worldSide? 1:0);
            int d_btm = (int)_btm & -(_btm <= _top ?1:0) & -(_btm < worldSide? 1:0);

            // Scale all changes to multiple of 8.
            d_lft = ((d_lft > 0?1:0) + (d_lft >> 3 )) << 3;
            d_rgt = ((d_rgt > 0?1:0) + (d_rgt >> 3)) << 3;
            d_top = ((d_top > 0?1:0) + (d_top >> 3)) << 3;
            d_btm = ((d_btm > 0?1:0) + (d_btm >> 3)) << 3;

            // Make sure plate doesn't grow bigger than the system it's in!
            if (width + d_lft + d_rgt > worldSide)
            {
                d_lft = 0;
                d_rgt = worldSide - width;
            }

            if (height + d_top + d_btm > worldSide)
            {
                d_top = 0;
                d_btm = worldSide - height;
            }

            if (d_lft + d_rgt + d_top + d_btm == 0) {
             //TODO exeption
            }

		int old_width = width;
		int old_height = height;

            left -= d_lft;
            left += left >= 0 ? 0 : worldSide;
            width += d_lft + d_rgt;

            top -= d_top;
            top += top >= 0 ? 0 : worldSide;
            height += d_top + d_btm;

//		printf("%ux%u + [%u,%u] + [%u, %u] = %ux%u\n",
//			old_width, old_height,
//			d_lft, d_top, d_rgt, d_btm, width, height);

            float[] tmph = new float[width*height];
            int[] tmpa = new int[width*height];
            int[] tmps = new int[width*height];

            // copy old plate into new.
            for (int j = 0; j < old_height; ++j)
            {
                int dest_i = (d_top + j) * width + d_lft;
                int src_i = j * old_width;

                tmph = Arrays.copyOf(map, old_width);
                tmpa = Arrays.copyOf(age, old_width);;
                tmps = Arrays.copyOf(segment, old_width);
            }

            map = tmph;
            age = tmpa;
            segment = tmps;

            // Shift all segment data to match new coordinates.
            for (int s = 0; s < segmentData.size(); ++s)
            {
                segmentData.get(s).x0 += d_lft;
                segmentData.get(s).x1 += d_lft;
                segmentData.get(s).y0 += d_top;
                segmentData.get(s).y1 += d_top;
            }

            index = getMapIndex(x, y);

            if (index >= width * height)
            {
               //TODO exeption
            }

        // Update crust's age.
        // If old crust exists, new age is mean of original and supplied ages.
        // If no new crust is added, original time remains intact.
	int old_crust = -(map[index] > 0 ? 1:0);
	int new_crust = -(z > 0? 1:0);
        t = (t & ~old_crust) | ((age[index] + t) / 2 & old_crust);
        age[index] = (t & new_crust) | (age[index] & ~new_crust);

        mass -= map[index];
        map[index] = z;		// Set new crust height to desired location.
        mass += z;		// Update mass c
    }

    @Override
    public float getMomentum() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public float getLeft() {
        return 0;
    }

    @Override
    public float getTop() {
        return 0;
    }

    @Override
    public float getVelocity() {
        return 0;
    }

    @Override
    public float getVelX() {
        return 0;
    }

    @Override
    public float getVelY() {
        return 0;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int createSegment(int wx, int wy) {
    int origin_index = wy * width + wx;
	int ID = segmentData.size();

        if (segment[origin_index] < ID)
            return segment[origin_index];

        boolean canGoLeft = wx > 0 && map[origin_index - 1] >= CONT_BASE;
        boolean canGoRight = wx < width - 1 && map[origin_index+1] >= CONT_BASE;
        boolean canGoUp = wy > 0 && map[origin_index - width] >= CONT_BASE;
        boolean canGoDown = wy < height - 1 &&
                map[origin_index + width] >= CONT_BASE;
        int nbour_id = ID;

        // This point belongs to no segment yet.
        // However it might be a neighbour to some segment created earlier.
        // If such neighbour is found, associate this point with it.
        if (canGoLeft && segment[origin_index - 1] < ID)
            nbour_id = segment[origin_index - 1];
        else if (canGoRight && segment[origin_index + 1] < ID)
            nbour_id = segment[origin_index + 1];
        else if (canGoUp && segment[origin_index - width] < ID)
            nbour_id = segment[origin_index - width];
        else if (canGoDown && segment[origin_index + width] < ID)
            nbour_id = segment[origin_index + width];

        if (nbour_id < ID)
        {
            segment[origin_index] = nbour_id;
            ++segmentData.get(nbour_id).area;

            if (wy < segmentData.get(nbour_id).y0) segmentData.get(nbour_id).y0 = wy;
            if (wy > segmentData.get(nbour_id).y1) segmentData.get(nbour_id).y1 = wy;
            if (wx < segmentData.get(nbour_id).x0) segmentData.get(nbour_id).x0 = wx;
            if (wx > segmentData.get(nbour_id).x1) segmentData.get(nbour_id).x1 = wx;

            return nbour_id;
        }

        int lines_processed;
        SegmentData data= new SegmentData(wx, wy, wx, wy, 0);

        ArrayList<Integer> spans_todo = new ArrayList<>();
        ArrayList<Integer> spans_done = new ArrayList<>();

        for (int i = 0; i <= height; i++){
            spans_todo.add(0);
            spans_done.add(0);
        }

        segment[origin_index] = ID;
        spans_todo.set(wy, wx);
        spans_todo.set(wy, wx);

        do
        {
            lines_processed = 0;
            for (int line = 0; line < height; ++line)
            {
                int start, end;

                if (spans_todo.get(line) == 0)
                    continue;

                do // Find an unscanned span on this line.
                {
                    end = spans_todo.get(line);

                    start = spans_todo.get(line);

                    // Reduce any done spans from this span.
                    for (int j = 0; j < spans_done.size();
                         j += 2)
                    {
                        // Saved coordinates are AT the point
                        // that was included last to the span.
                        // That's why equalities matter.

                        if (start >= spans_done.get(j) &&
                                start <= spans_done.get(j +1))
                            start = spans_done.get(j + 1) + 1;

                        if (end >= spans_done.get(j) &&
                                end <= spans_done.get(j))
                            end = spans_done.get(j + 1)- 1;
                    }

                    // Unsigned-ness hacking!
                    // Required to fix the underflow of end - 1.
                    start |= -(end >= width ? 1:0);
                    end -= (end >= width ? 1:0);

                } while (start > end);

                if (start > end) // Nothing to do here anymore...
                    continue;

                // Calculate line indices. Allow wrapping around map edges.
		int row_above = ((line - 1) & -(line > 0 ? 1:0))  |
                    ((height - 1) & -(line == 0 ? 1:0));
		int row_below = (line + 1) & -(line < height - 1? 1:0);
		int line_here = line * width;
		int line_above = row_above * width;
		int line_below = row_below * width;

                // Extend the beginning of line.
                while (start > 0 && segment[line_here+start-1] > ID &&
                        map[line_here+start-1] >= CONT_BASE)
                {
                    --start;
                    segment[line_here + start] = ID;

                    // Count volume of pixel...
                }

                // Extend the end of line.
                while (end < width - 1 &&
                        segment[line_here + end + 1] > ID &&
                        map[line_here + end + 1] >= CONT_BASE)
                {
                    ++end;
                    segment[line_here + end] = ID;

                    // Count volume of pixel...
                }

                // Check if should wrap around left edge.
                if (width == worldSide && start == 0 &&
                        segment[line_here+width-1] > ID &&
                        map[line_here+width-1] >= CONT_BASE)
                {
                    segment[line_here + width - 1] = ID;
                    spans_todo.add(width - 1);
                    spans_todo.add(width - 1);

                    // Count volume of pixel...
                }

                // Check if should wrap around right edge.
                if (width == worldSide && end == width - 1 &&
                        segment[line_here+0] > ID &&
                        map[line_here+0] >= CONT_BASE)
                {
                    segment[line_here + 0] = ID;
                    spans_todo.add(0);
                    spans_todo.add(0);

                    // Count volume of pixel...
                }

                data.area += 1 + end - start; // Update segment area counter.

                // Record any changes in extreme dimensions.
                if (line < data.y0) data.y0 = line;
                if (line > data.y1) data.y1 = line;
                if (start < data.x0) data.x0 = start;
                if (end > data.x1) data.x1 = end;

                if (line > 0 || height == worldSide)
                    for (int j = start; j <= end; ++j)
                        if (segment[line_above + j] > ID &&
                                map[line_above + j] >= CONT_BASE)
                        {
                            int a = j;
                            segment[line_above + a] = ID;

                            // Count volume of pixel...

                            while (++j < width &&
                                    segment[line_above + j] > ID &&
                                    map[line_above + j] >= CONT_BASE)
                            {
                                segment[line_above + j] = ID;

                                // Count volume of pixel...
                            }

                            int b = --j; // Last point is invalid.

                            spans_todo.add(a);
                            spans_todo.add(b);
                            ++j; // Skip the last scanned point.
                        }

                if (line < height - 1 || height == worldSide)
                    for (int j = start; j <= end; ++j)
                        if (segment[line_below + j] > ID &&
                                map[line_below + j] >= CONT_BASE)
                        {
                            int a = j;
                            segment[line_below + a] = ID;

                            // Count volume of pixel...

                            while (++j < width &&
                                    segment[line_below + j] > ID &&
                                    map[line_below + j] >= CONT_BASE)
                            {
                                segment[line_below + j] = ID;

                                // Count volume of pixel...
                            }

                            int b = --j; // Last point is invalid.

                            spans_todo.add(a);
                            spans_todo.add(b);
                            ++j; // Skip the last scanned point.
                        }

                spans_done.add(start);
                spans_done.add(end);
                ++lines_processed;
            }
        } while (lines_processed > 0);

        segmentData.add(data);
//	printf("Created segment [%u, %u]x[%u, %u]@[%u, %u].\n",
//		data.x0, data.y0, data.x1, data.y1, x, y);

        return ID;
    }

    public int getMapIndex(int px, int py) {
    //for future
        float irgt = left + width;
        float ibtm = top + height;

        if (px < worldSide - 1 && py < worldSide - 1) {
            int y = worldSide;
            int x = worldSide;

            x -= left;
            y -= top;

            return (y * width + x);
        }
        return 0;
    }

    private void checkIndex(int index){
        if (index >= width * height)
        {
            //TODO make exeption
        }
    }
}
