package com.rorlig.babyapp.ui.adapter;

import com.mobsandgeeks.adapters.Sectionizer;
import com.rorlig.babyapp.dao.MilestonesDao;

/**
 * @author gaurav gupta
 */
public class MilestoneSectionizer implements Sectionizer<MilestonesDao> {
    /**
     * Returns the title for the given instance from the data source.
     *
     * @param instance The instance obtained from the data source of the decorated list adapter.
     * @return section title for the given instance.
     */
    @Override
    public String getSectionTitleForItem(MilestonesDao instance) {
        return null;
    }
}